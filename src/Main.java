public class Main {

    public static void main(String[] args){

        //Integer[] nums = {-2, 0, 3, -5, 2, -1};
        String[] nums = {"2" , "0" , "3" , "-5" , "2" , "-1"};

        SegmentTree<String> segTree = new SegmentTree<>(nums, new Merger<String>() {
            @Override
            public String merge(String a, String b) {

                //return a + b; //求和
                return a + " " + b;
            }
        });

        System.out.println(segTree);
    }
}
