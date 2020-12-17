package com.wnn.system.domain.base;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BaseResult extends PageHelper {
    private Integer code;
    private String msg;
    private int page;
    private int size;
    private Long total;
    private Object data;

    public BaseResult(ResultCode code){
        this.code = code.code;
        this.msg = code.msg;
    }

    public BaseResult(Integer code, String msg){
        this.code = code;
        this.msg = msg;
    }


    public BaseResult(ResultCode code, Object data){
        if ( data instanceof PageInfo){
            this.total = ((PageInfo) data).getTotal();
            this.page = ((PageInfo) data).getPageNum();
            this.size = ((PageInfo) data).getPageSize();
            this.data =  ((PageInfo) data).getList();
        }else {
            this.data = data;
        }
        this.code = code.code;
        this.msg = code.msg;

    }
    public BaseResult(ResultCode code, Long count, Object data){
        this.code = code.code;
        this.msg = code.msg;
        this.data = data;
        if ( data instanceof PageInfo){
            this.total = ((PageInfo) data).getTotal();
            this.page = ((PageInfo) data).getPageNum();
            this.size = ((PageInfo) data).getPageSize();
            this.data =  ((PageInfo) data).getList();
        }
    }


    public static BaseResult ok(){ return new BaseResult(ResultCode.OK); }

    public static BaseResult failure(){
        return new BaseResult(ResultCode.FALSE);
    }

    public static BaseResult error(){
        return new BaseResult(ResultCode.ERROR);
    }
}
