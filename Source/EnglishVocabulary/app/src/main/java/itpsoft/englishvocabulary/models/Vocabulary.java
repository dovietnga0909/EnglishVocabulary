package itpsoft.englishvocabulary.models;

/**
 * Created by luand_000 on 05/06/2015.
 */
public class Vocabulary {
    private long id;
    private int cate_id;
    private String english, vietnamese;
    private boolean status_sync;

    public Vocabulary(long id, int cate_id, String english, String vietnamese, boolean status_sync) {
        this.id = id;
        this.cate_id = cate_id;
        this.english = english;
        this.vietnamese = vietnamese;
        this.status_sync = status_sync;
    }

    public Vocabulary(int cate_id, String english, String vietnamese, boolean status_sync) {
        this.cate_id = cate_id;
        this.english = english;
        this.vietnamese = vietnamese;
        this.status_sync = status_sync;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getCate_id() {
        return cate_id;
    }

    public void setCate_id(int cate_id) {
        this.cate_id = cate_id;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public String getVietnamese() {
        return vietnamese;
    }

    public void setVietnamese(String vietnamese) {
        this.vietnamese = vietnamese;
    }

    public boolean isStatus_sync() {
        return status_sync;
    }

    public void setStatus_sync(boolean status_sync) {
        this.status_sync = status_sync;
    }
}
