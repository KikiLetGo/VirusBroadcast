package state;

import life.Person;

/**
 * 确诊行为状态机
 */
public class ConfirmState implements State {
    private static final int STATE_CONFIRMED = 3;

    private Person person;

    public ConfirmState(){}

    public ConfirmState(Person person) {
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
    }

    @Override
    public void beConfirmed() {
        //person do something

        person.setState(this);
    }

    @Override
    public void beFreeze() {
    }

    @Override
    public void beDeath() {
    }
}
