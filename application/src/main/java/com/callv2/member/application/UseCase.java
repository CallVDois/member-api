package com.callv2.member.application;

public abstract class UseCase<IN, OUT> {

    public abstract OUT execute(IN anIn);

}
