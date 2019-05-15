package com.redblack.taksim.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListPump {
    public static HashMap<String, List<String>> getData() {
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

        List<String> cricket = new ArrayList<String>();
        cricket.add("Lorem ipsum");
        cricket.add("Lorem ipsum");
        cricket.add("Lorem ipsum");
        cricket.add("Lorem ipsum");
        cricket.add("Lorem ipsum");

        List<String> football = new ArrayList<String>();
        football.add("Lorem ipsum");
        football.add("Lorem ipsum");
        football.add("Lorem ipsum");
        football.add("Lorem ipsum");
        football.add("Lorem ipsum");

        List<String> basketball = new ArrayList<String>();
        basketball.add("Lorem ipsum");
        basketball.add("Lorem ipsum");
        basketball.add("Lorem ipsum");
        basketball.add("Lorem ipsum");
        basketball.add("Lorem ipsum");

        expandableListDetail.put("Lorem ipsum dolor sit amet,", cricket);
        expandableListDetail.put("Excepteur sint occaecat cupidatat", football);
        expandableListDetail.put("Sed ut perspiciatis unde omnis iste", basketball);
        expandableListDetail.put("Nemo enim ipsam voluptatem quia voluptas", basketball);
        expandableListDetail.put("Quis autem vel eum iure reprehenderit qui", basketball);
        return expandableListDetail;
    }
}
