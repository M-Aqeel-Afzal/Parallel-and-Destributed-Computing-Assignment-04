package question2;
import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class BSClass_Decipline {
	
  public static class BS_Mapper extends Mapper<Object, Text, Text, IntWritable>{

    private final static IntWritable one = new IntWritable(1);
    private Text word = new Text();

    public void map(Object key, Text value, Context context
                    ) throws IOException, InterruptedException {
    	String line = value.toString();
 
    	  String[] arrOfStr = line.split("@", 3);
          String[] str1 = line.split(",", 2);
          System.out.println(str1[0]);
      
          arrOfStr[1]=arrOfStr[1].substring(1, arrOfStr[1].length()-1);
          	if(!(arrOfStr[1].equals("Nill")))
          	{    	word.set(arrOfStr[1]);
              	 context.write(word, one);
          	}
           
              
    }
  }

  public static class BS_Reducer
       extends Reducer<Text,IntWritable,Text,IntWritable> {
    private IntWritable result = new IntWritable();
    String max_Key;
    int max=600;
    
    
    public void reduce(Text key, Iterable<IntWritable> values,
                       Context context
                       ) throws IOException, InterruptedException {
      int sum = 0;
      for (IntWritable val : values) {
        sum += val.get();
      }
      
     if(sum>=max)
      { 
    	  max=sum;
    	  result.set(max);
    	  context.write(key, result);
    	   	 
      }
    
     
    }
   
    
  }

  

  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    Job job = Job.getInstance(conf, "Max Decipiline");
    job.setJarByClass(BSClass_Decipline.class);
    job.setMapperClass(BS_Mapper.class);
    job.setCombinerClass(BS_Reducer.class);
    job.setReducerClass(BS_Reducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);
    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}