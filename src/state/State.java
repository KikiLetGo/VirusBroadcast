package state;

public interface State {
    /*int NORMAL = 0;//正常人，未感染的健康人
    int SUSPECTED = NORMAL + 1;//有暴露感染风险
    int SHADOW = SUSPECTED + 1;//潜伏期
    int CONFIRMED = SHADOW + 1;//发病且已确诊为感染病人
    int FREEZE = CONFIRMED + 1;//隔离治疗，禁止位移
    //已治愈出院的人转为NORMAL即可，否则会与作者通过数值大小判断状态的代码冲突
    int DEATH = FREEZE + 1;//病死者
    //int CURED = DEATH + 1;//治愈数量用于计算治愈出院后归还床位数量，该状态是否存续待定*/

    //行为
    /**
     * 成为正常人
     */
    void beNormal();

    /**
     * 成为感染者
     */
    void beSuspected();

    /**
     * 成为潜伏期者
     */
    void beShadow();

    /**
     * 成为确诊者
     */
    void beConfirmed();

    /**
     * 成为隔离者
     */
    void beFreeze();

    /**
     * 成为病死者
     */
    void beDeath();
}
