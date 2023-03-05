package question1;
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


public class FypClass {
	
  public static class FypMapper extends Mapper<Object, Text, Text, IntWritable>{

    private final static IntWritable one = new IntWritable(1);
    private Text word = new Text();

    public void map(Object key, Text value, Context context
                    ) throws IOException, InterruptedException {
    	String line = value.toString();
 
    	  String[] arrOfStr = line.split("@", 2);
          String[] str1 = line.split(",", 2);
          String[] str2 = arrOfStr[1].split(",", 2);
          	str2[1]=str2[1].replace('"',' ');
          		str2[1]=str2[1].substring(1, str2[1].length()-1);
            
              
              
              String[] str3 = str2[1].split(",", 15);
              String str4=str3[0],str5="";
              for(int i=1;i<str3.length;i++)
              {    str5=str4+" "+str3[i];
              	word.set(str5);
              	 context.write(word, one);
              str4=str3[i];
              }
              
    }
  }

  public static class FypReducer
       extends Reducer<Text,IntWritable,Text,IntWritable> {
    private IntWritable result = new IntWritable();
    String max_Key;
    int max=9;
    
    
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
    /* String line = key.toString();
     if("sklearn node".equals(line))
	 {  result.set(max);
	 	context.write(max_Key_value, result);
	 }*/
     
    }
   
    
  }

  

  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    Job job = Job.getInstance(conf, "Fyp ");
    job.setJarByClass(FypClass.class);
    job.setMapperClass(FypMapper.class);
    job.setCombinerClass(FypReducer.class);
    job.setReducerClass(FypReducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);
    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}