package state;

import life.Person;

/**
 * 死亡行为状态机
 */
public class DeathState implements State {
    private static final int STATE_DEATH = 5;

    private Person person;

    public DeathState(){}
    public DeathState(Person person) {
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
    }

    @Override
    public void beDeath() {
        //person do something

        person.setState(this);
    }
}
