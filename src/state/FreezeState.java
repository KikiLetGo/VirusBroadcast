package state;

import life.Person;

/**
 * 隔离治疗行为状态机
 */
public class FreezeState implements State {
    private static final int STATE_FREEZE = 4;

    private Person person;

    public FreezeState(){}
    public FreezeState(Person person) {
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
    }

    @Override
    public void beFreeze() {
        //person do something

        person.setState(this);
    }

    @Override
    public void beDeath() {
    }
}
