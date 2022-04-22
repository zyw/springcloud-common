package cn.v5cn.flink19.transformations;

/**
 * @author ZYW
 * @version 1.0
 * @date 2020-02-20 22:20
 */
public class WordCount {
    private String word;
    private Integer counts;

    public WordCount() {
    }

    public WordCount(String word, Integer counts) {
        this.word = word;
        this.counts = counts;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Integer getCounts() {
        return counts;
    }

    public void setCounts(Integer counts) {
        this.counts = counts;
    }

    @Override
    public String toString() {
        return "WordCount{" +
                "word='" + word + '\'' +
                ", counts=" + counts +
                '}';
    }
}
