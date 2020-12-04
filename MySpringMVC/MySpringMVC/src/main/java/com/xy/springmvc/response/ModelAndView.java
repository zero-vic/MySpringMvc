package com.xy.springmvc.response;

/**
 * Created With IntelliJ IDEA
 * User: yao
 * Date:2020/11/23
 * Description:
 * Version:V1.0
 */
public class ModelAndView<T> {
    private int code;
    private String msg;
    private T obj;
    private String view;

    public ModelAndView() {
    }

    public ModelAndView(String view) {
        this.view = view;
    }

    public ModelAndView(ResponseCodeInterface rc, T obj, String view) {
        this.code = rc.getCode();
        this.msg = rc.getMsg();
        this.obj = obj;
        this.view = view;
    }

    public void setRc(ResponseCodeInterface rc) {
        this.code = rc.getCode();
        this.msg = rc.getMsg();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getObj() {
        return obj;
    }

    public void setObj(T obj) {
        this.obj = obj;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    @Override
    public String toString() {
        return "ModelAndView{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", obj=" + obj +
                ", view='" + view + '\'' +
                '}';
    }
}
