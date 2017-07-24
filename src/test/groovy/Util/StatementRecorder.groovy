package Util

public class StatementRecorder implements GroovyInterceptable {
  public static final int INVOKE_METHOD = 0
  public static final int GET_PROPERTY = 1

  def context
  List propAndInvocations = []

  private addMethodDesc(String name, args) {
    propAndInvocations << [type:INVOKE_METHOD, name:name, args:args]
  }

  private addPropertyDesc(String propertyName) {
    propAndInvocations << [type:GET_PROPERTY, propertyName:propertyName]
  }

  public StatementRecorder(def context) {
    this.context = context
  }

  @Override
  public Object invokeMethod(String name, Object args) {
    MetaMethod metaMethod = StatementRecorder.metaClass.getMetaMethod(name, args)
    if (metaMethod) {
      return metaMethod.invoke(this, args)
    }

    addMethodDesc(name, args)
    this
  }

  @Override
  public Object getProperty(String propertyName) {
    MetaProperty metaProperty = StatementRecorder.metaClass.getMetaProperty(propertyName)
    if (metaProperty) {
      return metaProperty.getProperty(this)
    }

    addPropertyDesc(propertyName)
    this
  }

  @Override
  public void setProperty(String propertyName, Object newValue) {
    throw new ReadOnlyPropertyException(propertyName, StatementRecorder)
  }

  public boolean isValid() {
    propAndInvocations.last().type == INVOKE_METHOD
  }

  public invokee() {
    assert propAndInvocations.size() >= 2

    def current = context
    def fetchObject = { d ->
      current = (d.type == INVOKE_METHOD) ? current."$d.name"(*d.args) : current."$d.propertyName"
    }
    propAndInvocations[0..-2].each(fetchObject)
    current
  }

  public void replay() {
    def invocation = propAndInvocations.last()
    MetaMethod metaMethod = invokee().metaClass.getMetaMethod(invocation.name, invocation.args)
    assert metaMethod
    metaMethod.invoke(invokee(), invocation.args)
  }

  @Override
  public String toString() {
    Closure methodToString = { def methodDescriptor ->
      assert methodDescriptor.type == INVOKE_METHOD
      assert methodDescriptor.keySet().contains("name")

      "${methodDescriptor.get('name')}(${methodDescriptor.get('args') ? methodDescriptor.get('args').join(', ') : ''})"
    }
    Closure propertyToString = { def propertyDescriptor ->
      assert propertyDescriptor.type == GET_PROPERTY
      assert propertyDescriptor.keySet().contains('propertyName')

      propertyDescriptor.get('propertyName')
    }
    Closure memberToString = { def memberDescriptor ->
      switch (memberDescriptor.type) {
        case INVOKE_METHOD: return methodToString(memberDescriptor)
        case GET_PROPERTY: return propertyToString(memberDescriptor)
        default: throw new IllegalStateException()
      }
    }

    propAndInvocations.collect(memberToString).join('.')
  }
}
