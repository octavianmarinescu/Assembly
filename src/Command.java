
public enum Command {

    JGZ("jgz"),
    SET("set"),
    ADD("add"),
    MUL("mul"),
    MOD("mod"),
    SND("snd"),
    RCV("rcv");

    private String description;

    Command(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

}
