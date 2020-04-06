package state;

import life.Person;

/**
 * 潜伏行为状态机
 */
public class ShadowState implements State {
    private static final int STATE_SHADOW = 2;

    private Person person;

    public ShadowState(){}
    public ShadowState(Person person) {
        this.person = person;
    }

    @Override
    public void beNormal() {
    }

    @Override
    public void beSuspected() {
    }

    @Override
    public void beShadow() {
        //person do something

        person.setState(this);
    }

    @Override
    public void beConfirmed() {
    }

    @Override
    public void beFreeze() {
    }

    @Override
    public void beDeath() {
    }
}
