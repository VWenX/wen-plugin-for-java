package cn.xuwen.common.util;


import cn.xuwen.common.annotation.Note;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * 集合比较器
 * @author XuWen
 */
public class CollectionCompare {

    @Note("对比集合差异")
    public static <E> ChangeInfo<E> compareDiff(Collection<E> oldColl, Collection<E> newColl){
        if (oldColl == null) oldColl = new ArrayList<>(0);
        if (newColl == null) newColl = new ArrayList<>(0);

        HashSet<E> oldSet = new HashSet<>(oldColl);

        Set<E> adds = new HashSet<>();
        for (E e : new HashSet<>(newColl)) { // 去重后遍历，避免循环内元素二次remove不到误判为新增
            if (! oldSet.remove(e)) adds.add(e);
        }

        return new ChangeInfo<E>(adds, oldSet);
    }


    public static class ChangeInfo<E>{
        public Set<E> adds;
        public Set<E> removes;
        public ChangeInfo(Set<E> adds, Set<E> removes) {
            this.adds = adds;
            this.removes = removes;
        }
    }

}
