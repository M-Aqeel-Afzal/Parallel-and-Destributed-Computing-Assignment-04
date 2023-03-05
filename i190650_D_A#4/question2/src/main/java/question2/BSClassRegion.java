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

public class BSClassRegion {
	
  public static class BS_Mapper extends Mapper<Object, Text, Text, IntWritable>{

    private final static IntWritable one = new IntWritable(1);
    private Text word = new Text();

    public void map(Object key, Text value, Context context
                    ) throws IOException, InterruptedException {
    	String line = value.toString();
    	String punjab="Lahore,Faisalabad,Rawalpindi,Gujranwala,Multan,Bahawalpur,Sargodha,Sialkot City,Chiniot,Shekhupura,Jhang City,Dera Ghazi Khan,Gujrat,Rahimyar Khan,Kasur,Sahiwal,Okara,Mandi Burewala,Saddiqabad,Muridke,Muzaffargarh,Khanpur,Gojra,Mandi Bahauddin,Bahawalnagar,Pakpattan,Ahmadpur East,Vihari,Jaranwala,Kamalia,Kot Addu,Khushab,Chishtian,Hasilpur,Attock,Khurd,Mianwali,Jalalpur Jattan,Bhakkar,Dipalpur,Kharian,Mian Channun,Bhalwal,Pattoki,Harunabad,Kahror Pakka,Toba Tek Singh,Samundri,Shakargarh,Sambrial,Shujaabad,Hujra Shah Muqim,Kabirwala,Lala Musa,Chunian,Nankana Sahib,Pasrur,Chenab Nagar,Abdul Hakim,Hassan Abdal,Kundian,Narowal,Khanewal"
    			+ ",Jhelum,Hafizabad,Lodhran,Attock City,Leiah,Chakwal,Rajanpur";
String sindh ="Karachi,Hyderabad City,Sukkur,Larkana,Nawabshah,Mirpur Khas,Jacobabad,Dadu,Tando Allahyar,Kandhkot,Jamshoro,Umarkot,Khairpur Mirâ€™s,Shikarpur,Matiari,Ghotki,Naushahro Firoz,Tando Muhammad Khan,Badin,Shahdad Kot,Sanghar,Thatta"; 
String kpk ="Peshawar,Saidu Sharif,Mardan,Mingaora,Kohat,Abbottabad,Nowshera,Swabi,Dera Ismail Khan,Charsadda,Mansehra,Bannu,Timargara,Parachinar,Tank,Hangu,Risalpur Cantonment,Karak,Chitral,Kulachi,Haripur,Malakand,Batgram,Alpurai,Daggar,Lakki,Upper Dir,Dasu";
String bloch = "Quetta,Turbat,Khuzdar,Chaman,Zhob,Gwadar,Kalat,Dera Allahyar,Pishin,Dera Murad Jamali,Kohlu,Mastung,Loralai,Barkhan,Musa Khel Bazar,Ziarat,Gandava,Sibi,Dera Bugti,Uthal,Khuzdar,Panjgur,Qila Saifullah,Kharan,Awaran,Dalbandin";
String[] a1 = punjab.split(",", 75);
String[] a2 = sindh.split(",", 75);
String[] a3 = kpk.split(",", 75);
String[] a4 = bloch.split(",", 75);

    			
    	  String[] arrOfStr = line.split("@", 3);
          String[] str1 = line.split(",", 2);
          System.out.println(str1[0]);
       
          arrOfStr[1]=arrOfStr[1].substring(1, arrOfStr[1].length()-1);
          for(int i=0;i<a1.length;i++)
          {	if(arrOfStr[1].equals(a1[i]))
          	{    	word.set("Punjab");
              	 context.write(word, one);
          	}
          }
          for(int i=0;i<a2.length;i++)
          {	if(arrOfStr[1].equals(a2[i]))
          	{    	word.set("Sindh");
              	 context.write(word, one);
          	}
          }
          
          for(int i=0;i<a3.length;i++)
          {	if(arrOfStr[1].equals(a3[i]))
          	{    	word.set("KPK");
              	 context.write(word, one);
          	}
          }
          
          for(int i=0;i<a4.length;i++)
          {	if(arrOfStr[1].equals(a4[i]))
          	{    	word.set("Blochistan");
              	 context.write(word, one);
          	}
          }
          if(arrOfStr[1].equals("Islamabad"))
        	{    	word.set("Fedral");
            	 context.write(word, one);
        	}
          if(arrOfStr[1].equals("Others"))
        	{    	word.set("Others");
            	 context.write(word, one);
        	}
          
        
              
    }
  }

  public static class BS_Reducer
       extends Reducer<Text,IntWritable,Text,IntWritable> {
    private IntWritable result = new IntWritable();
    String max_Key;
    int max=3000;
    
    
    public void reduce(Text key, Iterable<IntWritable> values,
                       Context context
                       ) throws IOException, InterruptedException {
      int sum = 0;
      for (IntWritable val : values) {
        sum += val.get();
      }
      
     if(sum>=2)
      { 
    	  max=sum;
    	  result.set(max);
    	  context.write(key, result);
    	   	 
      }
   
     
    }
   
    
  }

  

  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    Job job = Job.getInstance(conf, "Region");
    job.setJarByClass(BSClassRegion.class);
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