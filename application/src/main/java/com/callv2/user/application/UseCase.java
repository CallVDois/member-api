package com.callv2.user.application;

public abstract class UseCase<IN, OUT> {

    public abstract OUT execute(IN anIn);

}
