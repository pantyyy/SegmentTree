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


    //用户输入区间范围 , 从这棵树中进行查询
    public E query(int queryL , int queryR){
        //进行输入的合法性判断
        //1.queryL 不能大于 queryR
        //2.queryL和queryR不能小于0
        //3.queryL和queryR的不能大于区间的最大值
        if(queryL < 0 || queryL >= data.length ||
                queryR < 0 || queryR >= data.length || queryL > queryR)

            throw new IllegalArgumentException("Index is illegal.");

        //调用递归方法进行查询
        //参数 : 1.开始的数根下标 2.这棵树对应的左右区间范围 , 3.用户输入的区间范围
        return query(0 , 0 , data.length - 1 , queryL , queryR);
    }




    private E query(int treeIndex, int l, int r, int queryL, int queryR) {

        //递归结束的条件
        if(l == queryL && r == queryR){
            return tree[treeIndex];
        }

        //向下寻找有三种情况
        //1.用户输入的区间在左子树中
        //2.用户输入的区间在右子树中
        //3.用户输入的区间在两个数中都有

        int mid = l + (r - l) / 2;
        //计算左右子树的数组下标
        int leftIndex = leftChild(treeIndex);
        int rightIndex = rightChild(treeIndex);

        //进行情况的判断
        if(queryR <= mid){
            //在左子树中
            return query(leftIndex , l , mid , queryL , queryR);
        }else if(queryL >= mid + 1){
            //在右子树中
            return query(rightIndex , mid + 1 , r , queryL , queryR);
        }

        //左右子树都有 , 分别从左右子树中寻找解 , 然后合并解 , 返回即可
        E leftResult = query(leftIndex , l , mid , queryL , mid);
        E rightResult = query(rightIndex , mid + 1 , r , mid + 1 , queryR);

        return merger.merge(leftResult , rightResult);
    }


    //将数组中index位置的值 , 更新为e
    public void set(int index , E e){
        //判断index的合法性
        if(index < 0 || index >= data.length){
            throw new IllegalArgumentException("Index is illegal");
        }

        data[index] = e;

        set(0 , 0 , data.length - 1, index , e);
    }

    //在以treeIndex为根的线段树中更新index的值为e
    private void set(int treeIndex , int l , int r , int index , E e){
        if(l == r){
            tree[treeIndex] = e;
            return;
        }

        int mid = l + (r - l) / 2;
        int leftIndex = leftChild(treeIndex);
        int rightIndex = rightChild(treeIndex);

        //判断应该在哪个子树中进行更新
        if(index <= mid){
            set(leftIndex , l , mid , index , e);
        }else{
            set(rightIndex , mid + 1 , r , index , e);
        }

        //因为孩子节点的值发生了改变 , 所以 , 当前节点也应该发生了变化
        tree[treeIndex] = merger.merge(tree[leftIndex] , tree[rightIndex]);
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
