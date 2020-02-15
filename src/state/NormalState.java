package state;

import life.Person;

/**
 * 正常行为状态机
 */
public class NormalState implements State {
    private static final int STATE_NORMAL = 0;

    private Person person;

    public NormalState(){}
    public NormalState(Person person) {
        this.person = person;
    }

    @Override
    public void beNormal() {
        //person do something

        person.setState(this);
    }

    @Override
    public void beSuspected() {
    }

    @Override
    public void beShadow() {
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
