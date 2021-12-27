package com.example.trashclassify.tools.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TrashTest {
    /**
     * code : 200
     * msg : success
     * newslist : [{"name":"羽毛球","type":3,"aipre":0,"explain":"干垃圾即其它垃圾，指除可回收物、有害垃圾、厨余垃圾（湿垃圾）以外的其它生活废弃物。","contain":"常见包括砖瓦陶瓷、渣土、卫生间废纸、猫砂、污损塑料、毛发、硬壳、一次性制品、灰土、瓷器碎片等难以回收的废弃物","tip":"尽量沥干水分；难以辨识类别的生活垃圾都可以投入干垃圾容器内"},{"name":"羽毛球拍","type":0,"aipre":0,"explain":"可回收垃圾是指适宜回收、可循环利用的生活废弃物。","contain":"常见包括各类废金属、玻璃瓶、易拉罐、饮料瓶、塑料玩具、书本、报纸、广告单、纸板箱、衣服、床上用品、电子产品等","tip":"轻投轻放；清洁干燥，避免污染，费纸尽量平整；立体包装物请清空内容物，清洁后压扁投放；有尖锐边角的、应包裹后投放"}]
     */

    private int code;
    private String msg;
    private NewslistBean newslist = new NewslistBean();

    public void setList(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            this.code = jsonObject.getInt("code");
            this.msg = jsonObject.getString("msg");
            JSONArray jsonObject1 = jsonObject.getJSONArray("newslist");
            NewslistBean newslistBean = new NewslistBean();
            JSONObject object = (JSONObject) jsonObject1.get(0);
            newslistBean.setName(object.getString("name"));
            newslistBean.setType(object.getInt("type"));
            newslistBean.setExplain(object.getString("explain"));
            newslist = newslistBean;

            //Log.i("json", this.code + "");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("json", e.toString());
        }

        //this.setCode(result.);
    }

    /*
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    */

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public NewslistBean getNewslist() {
        return newslist;
    }

    public String getNewslistName() {
        return newslist.name;
    }
    /*
    public void setNewslist(List<NewslistBean> newslist) {
        this.newslist = newslist;
    }

     */

    public static class NewslistBean {
        /**
         * name : 羽毛球
         * type : 3
         * aipre : 0
         * explain : 干垃圾即其它垃圾，指除可回收物、有害垃圾、厨余垃圾（湿垃圾）以外的其它生活废弃物。
         * contain : 常见包括砖瓦陶瓷、渣土、卫生间废纸、猫砂、污损塑料、毛发、硬壳、一次性制品、灰土、瓷器碎片等难以回收的废弃物
         * tip : 尽量沥干水分；难以辨识类别的生活垃圾都可以投入干垃圾容器内
         */

        private String name;
        private int type;
        private String explain;

        private String[] garbage_type_string={"可回收垃圾","有害垃圾","厨余垃圾","其它垃圾（干垃圾）"};

        public String getType(){
            return garbage_type_string[type];
        }

        public int getTypeInt() {return type;}

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getExplain() {
            return explain;
        }

        public void setExplain(String explain) {
            this.explain = explain;
        }
    }
}
