package algonquin.cst2335.finalproject.bean;

import java.io.Serializable;

/**
 * 收藏的实体类/The entity class of the collection/Serializable refers to the ability of an
 * object or data structure to be converted into a format that can be easily transmitted
 * across a network or saved to disk for later use. Serializable data can be transmitted
 * between different systems or processes,
 * and can be stored in a standardized format that can be easily read and manipulated by
 * different programs or applications.
 */
public class CollectionBean implements Serializable {
    private String id;
    private String title;
    private String content;
    private String web_url;
    private String type;
    private String time;
    private String come;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCome() {
        return come;
    }

    public void setCome(String come) {
        this.come = come;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getWeb_url() {
        return web_url;
    }

    public void setWeb_url(String web_url) {
        this.web_url = web_url;
    }
}