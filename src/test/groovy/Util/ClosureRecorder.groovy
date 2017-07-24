package Util

public class ClosureRecorder implements GroovyInterceptable {
  def context
  List<StatementRecorder> statements = []

  public ClosureRecorder(def context) {
    this.context = context
  }

  private delegateInvokeMethod(String name, args) {
    def rec = new StatementRecorder(context)
    rec."$name"(*args)
    statements << rec
    rec
  }

  private delegateGetProperty(String propertyName) {
    def rec = new StatementRecorder(context)
    rec."$propertyName"
    statements << rec
    rec
  }

  @Override
  public Object invokeMethod(String name, Object args) {
    MetaMethod metaMethod = ClosureRecorder.metaClass.getMetaMethod(name, args)
    metaMethod ? metaMethod.invoke(this, args) : delegateInvokeMethod(name, args)
  }

  @Override
  public Object getProperty(String propertyName) {
    MetaProperty metaProperty = ClosureRecorder.metaClass.getMetaProperty(propertyName)
    metaProperty ? metaProperty.getProperty(this) : delegateGetProperty(propertyName)
  }

  @Override
  public void setProperty(String propertyName, Object newValue) {
    throw new ReadOnlyPropertyException(propertyName, StatementRecorder)
  }

  public boolean isValid() {
    statements*.valid.every()
  }

  public void replay() {
    if (!valid) {
      throw new IllegalStateException('The closure recorded is not valid so cannot be replayed')
    }

    for (int i = 0; i < statements.size() - 1; i++) {
      if (!statements[i + 1].invokee().displayed) {
        statements[i].replay()
        context.waitFor { statements[i + 1].invokee().displayed }
      }
    }

    statements.last().replay()
  }

  List<String> toStatements() {
    statements*.toString()
  }

  public String toString() {
    toStatements().join(";")
  }
}