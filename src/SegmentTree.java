public class SegmentTree<E> {

    private E[] data;   //用户传入的数组
    private E[] tree;   //线段树(数组表示)
    private Merger<E> merger;   //融合器


    //传入一个数组 , 用线段树表示
    public SegmentTree(E[] arr , Merger<E> merger){

        this.merger = merger;

        data = (E[])new Object[arr.length];
        for(int i = 0 ; i < arr.length ; i++)
            data[i] = arr[i];

        //使用线段树表示 , 需要要数组的空间为4n
        tree = (E[])new Object[4 * arr.length];
        buildSegmentTree(0 , 0 , arr.length - 1);
    }

    //在treeIndex的位置创建表示区间[l...r]的线段树
    private void buildSegmentTree(int treeIndex, int l, int r) {

        //递归结束的条件 , 区间的大小为1时 , 也就是l等于r时
        if(l == r){
            tree[treeIndex] = data[l];
            return;
        }

        //想要构建以treeIndex为根节点的线段树
        //首先要构建以treeIndex的左孩子为根节点的线段树
        //然后构建以treeInex的右孩子为根节点的线段树
        //最后把treeIndex的左右孩子用来融合为根节点

        //1.构建以treeIndex的左孩子为根节点的线段树
        //2.需要调用buildSegmentTree , 同时需要知道左孩子的treeIndex和l , r
        int leftTreeIndex = leftChild(treeIndex);
        int rightTreeIndex = rightChild(treeIndex);

        int mid = l + (r - l) / 2;  //计算出中间值
        buildSegmentTree(leftTreeIndex , l , mid);
        buildSegmentTree(rightTreeIndex , mid + 1 , r);

        //根据具体的业务需求 , 融合成根节点
        //tree[treeIndex] = tree[leftTreeIndex] + tree[rightTreeIndex];
        tree[treeIndex] = merger.merge(tree[leftTreeIndex] , tree[rightTreeIndex]);
    }

    public int getSize(){
        return data.length;
    }

    public E get(int index){
        if(index < 0 || index >= data.length)
            throw new IllegalArgumentException("Index is illegal.");
        return data[index];
    }


    private int leftChild(int index){
        return index * 2 + 1;
    }
    private int rightChild(int index){
        return index * 2 + 2;
    }


    @Override
    public String toString(){
        StringBuilder res = new StringBuilder();
        res.append('[');

        for(int i = 0 ; i < tree.length - 1 ; i++){
            if(tree[i] != null)
                res.append(tree[i]);
            else
                res.append("null");

            if(i != tree.length - 1)
                res.append(", ");
        }

        res.append("]");
        return res.toString();
    }



}
