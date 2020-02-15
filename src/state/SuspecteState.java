package state;

import life.Person;

/**
 * 暴露行为状态机
 */
public class SuspecteState implements State {
    private static final int STATE_SUSPECTED = 1;

    private Person person;

    public SuspecteState(){}
    public SuspecteState(Person person) {
        this.person = person;
    }

    @Override
    public void beNormal() {
    }

    @Override
    public void beSuspected() {
        //person do something

        person.setState(this);
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
