package model.db;

public class Wrapper {

    private int count;

    private String content;

    private Integer tre;

    public Wrapper(int count, String content) {
        this.count = count;
        this.content = content;
        this.tre = 42;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getTre() {
        return tre;
    }

    public void setTre(Integer tre) {
        this.tre = tre;
    }
}
