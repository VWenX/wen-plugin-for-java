package cn.xuwen.common.util;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author XuWen
 */
public class TreeUtils {

    /**
     * 生成树
     * @param list 源数据集合
     * @param toTreeConvert 源数据到树节点的转换器
     * @param idType  id的类型，避免不正确的类型装箱与推断失误
     * @param idGetter id获取器
     * @param parentIdGetter 父id获取器
     * @param topParentId  顶级数据的父id
     * @param childrenSetter 子级设置器
     * @param <E> 源数据类型
     * @param <R> 树节点类型
     * @return 树
     */
    public static <E, R, ID> List<R> gen(Collection<E> list, Function<E, R> toTreeConvert,
                                         Class<ID> idType, Function<R, ID> idGetter,
                                         Function<E, ID> parentIdGetter, ID topParentId,
                                         BiConsumer<R, List<R>> childrenSetter){
        // 按父级标记分组 并将子级转换为树实体
        Map<ID, List<R>> pListMap = list.stream().collect(Collectors.groupingBy(
                parentIdGetter,
                Collectors.mapping(toTreeConvert, Collectors.toList())
        ));

        // 取顶树
        List<R> treeTop = pListMap.get(topParentId);
        if (treeTop == null){
            return new ArrayList<>(0);
        }

        // 填充树实体的子级
        for (List<R> trees : pListMap.values()) {
            for (R tree : trees) {
                List<R> children = pListMap.get(idGetter.apply(tree));
                if (children == null) children = new ArrayList<>(0);
                childrenSetter.accept(tree, children);
            }
        }

        // 取顶树
        return treeTop;
    }

    /**
     * 树转列 注：节点仍保持引用
     * @param tree 树
     * @param childrenGetter 子级获取器
     * @param <T> 节点类型
     * @return 所有节点
     */
    public static <T> List<T> toList(Collection<T> tree, Function<T, Collection<T>> childrenGetter){
        if (tree == null){
            return new ArrayList<>(0);
        }

        // 结果集
        List<T> list = new ArrayList<>();
        // 待查栈 初始入顶层树节点
        LinkedList<T> needFindStack = new LinkedList<>(tree);

        while (! needFindStack.isEmpty()){
            T node = needFindStack.pop();
            if (node == null){
                continue;
            }

            // 加入结果集
            list.add(node);

            // 子级加入待查栈
            Collection<T> children = childrenGetter.apply(node);
            if (children != null){
                needFindStack.addAll(children);
            }
        }

        return list;
    }


    /**
     * 树类型转换
     * @param tree 原树
     * @param convert 原树节点到新树节点的转换器 无需处理子级
     * @param childrenGetter 原树的子级获取器
     * @param childrenSetter 新树的子级设置器
     * @param <T> 原树节点类型
     * @param <R> 新树节点类型
     * @return 新树
     */
    public static <T, R> List<R> convertTree(Collection<T> tree, Function<T, R> convert,
                                             Function<T, Collection<T>> childrenGetter,
                                             BiConsumer<R, List<R>> childrenSetter){
        if (tree == null){
            return null;
        }

        List<R> newTree = new ArrayList<>(tree.size());
        for (T node : tree) {
            // 转换
            R newNode = convert.apply(node);
            newTree.add(newNode);
            // 子级处理 TODO:暂时简单递归，后期待优化为栈式处理
            Collection<T> children = childrenGetter.apply(node);
            if (children != null){
                List<R> newChildren = convertTree(children, convert, childrenGetter, childrenSetter);
                childrenSetter.accept(newNode, newChildren);
            }
        }
        return newTree;
    }



}
