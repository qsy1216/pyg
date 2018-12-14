package com.pinyougou.search.service;

import java.util.List;
import java.util.Map;

public interface SearchService {


        /**
         * 搜索方法
         * @param searchMap
         * @return
         */
        public Map<String, Object> search(Map<String, Object> searchMap);

        /**
         * 导入列表
         * @param list
         */
        public void importList(List list);

        /**
         * 删除数据
         * @param
         */
        public void deleteByGoodsIds(List goodsIdList);

}
