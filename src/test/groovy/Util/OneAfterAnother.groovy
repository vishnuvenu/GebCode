package Util

public class OneAfterAnother {
  public static void oneAfterAnother(def context, Closure definition) {
    ClosureRecorder recorder = new ClosureRecorder(context)
    definition.setResolveStrategy(Closure.DELEGATE_ONLY)
    definition.delegate = recorder
    definition.call()     //Record closure actions

    recorder.replay()     //Replay closure actions
  }
}
