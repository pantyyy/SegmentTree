//303. 区域和检索 - 数组不可变
//给定一个整数数组  nums，求出数组从索引 i 到 j  (i ≤ j) 范围内元素的总和，包含 i,  j 两点。
//
//        示例：
//
//        给定 nums = [-2, 0, 3, -5, 2, -1]，求和函数为 sumRange()
//
//        sumRange(0, 2) -> 1
//        sumRange(2, 5) -> -1
//        sumRange(0, 5) -> -3
//        说明:
//
//        你可以假设数组不可变。
//        会多次调用 sumRange 方法。


class NumArray {

    private SegmentTree<Integer> segmentTree;
    public NumArray(int[] nums) {
        //根据用户传入的数组构建线段树
        Integer[] data = new Integer[nums.length];
        for (int i = 0; i < nums.length; i++)
            data[i] = nums[i];
        segmentTree = new SegmentTree<>(data, new Merger<Integer>() {
            @Override
            public Integer merge(Integer a, Integer b) {
                return a + b;
            }
        });
        }

    public int sumRange(int i, int j) {
        if(segmentTree == null){
            throw new IllegalArgumentException("Segment Tree is null");
        }

        return segmentTree.query(i , j);
    }


    public static void main(String[] args){

        int nums[] = {-2, 0, 3, -5, 2, -1};

        NumArray numArray = new NumArray(nums);
        System.out.println(numArray.sumRange(0 , 2));
        System.out.println(numArray.sumRange(2 , 5));
        System.out.println(numArray.sumRange(0 , 5));
    }
}


