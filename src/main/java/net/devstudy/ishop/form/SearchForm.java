package net.devstudy.ishop.form;

import java.util.ArrayList;
import java.util.List;

public class SearchForm {
    private String query;
    private List<Integer> category;
    private List<Integer> producer;

    public SearchForm(String query, String[] category, String[] producer) {
        super();
        this.query = query;
        this.category = convert(category);
        this.producer = convert(producer);
    }

    private List<Integer> convert(String[] args){
        if (args == null){
            return null;
        }else {
            List<Integer> res = new ArrayList<>();
            for(String arg: args){
                res.add(Integer.parseInt(arg));
            }
            return res;
        }

    }


    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<Integer> getCategory() {
        return category;
    }

    public void setCategory(List<Integer> category) {
        this.category = category;
    }

    public List<Integer> getProducer() {
        return producer;
    }

    public void setProducer(List<Integer> producer) {
        this.producer = producer;
    }
}
