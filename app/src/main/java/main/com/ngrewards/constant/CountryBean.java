package main.com.ngrewards.constant;

/**
 * Created by technorizen on 2/7/18.
 */

public class CountryBean {
    String id;
    String sortname;
    String name;
    String flag_url;

    public String getFlag_url() {
        return flag_url;
    }

    public void setFlag_url(String flag_url) {
        this.flag_url = flag_url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSortname() {
        return sortname;
    }

    public void setSortname(String sortname) {
        this.sortname = sortname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
