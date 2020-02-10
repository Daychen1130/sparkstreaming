# sparkstreaming
demo
set mapred.job.name=tdm_wit_market_convert_new(${hivevar:statis_date});
set mapred.max.split.size=128000000;
set mapred.min.split.size.per.node=1;
set mapred.min.split.size.per.rack=1;
set hive.input.format=org.apache.hadoop.hive.ql.io.CombineHiveInputFormat;
set hive.exec.reducers.bytes.per.reducer=100000000;
set hive.exec.reducers.max=300;
set hive.map.aggr=true;
set hive.groupby.skewindata=true;
set hive.groupby.mapaggr.checkinterval=100000;
set hive.map.aggr.hash.min.reduction=0.5;




set hive.map.aggr=true;
set hive.auto.convert.join=true;
set hive.mapjoin.smalltable.filesize=25000000;

set mapred.job.name = sosp_label_retail_visit_info(20190916);

set hive.auto.convert.join = true;

set mapreduce.input.fileinputformat.split.maxsize=256000000;
set mapreduce.input.fileinputformat.split.minsize=64000000;
set hive.merge.size.per.task=256000000;

--控制map数量、合并输入文件

set mapred.max.split.size=256000000;
set mapred.min.split.size.per.node=1;
set mapred.min.split.size.per.rack=1;


--1.每个Map最大输入大小
set mapred.max.split.size=256000000;
--2..每个Map最小输入大小
set mapred.min.split.size=128000000;
--3.一个节点上split的至少的大小
set mapred.min.split.size.per.node=128000000;
--4.一个交换机上split的至少的大小
set mapred.min.split.size.per.rack=64000000;
--5.执行Map前进行小文件合并
set hive.input.format = org.apache.hadoop.hive.ql.io.CombineHiveInputFormat;



--合并输入输出文件
--1.在Map-only的任务结束时合并小文件
set hive.merge.mapfiles = true 
--2.在Map-Reduce的任务结束时合并小文件
set hive.merge.mapredfiles = true
--3.合并文件的大小
有两个配合参数：
--合并之后的每个文件大小
set hive.merge.size.per.task=128000000;    
--判断是否开启合并，如果输出文件的平均大小小于改值则开启合并(hdfs上文件总大小/文件个数)
set hive.merge.smallfiles.avgsize=16000000;



--调整reduce 数量
set hive.exec.reducers.bytes.per.reducer=128000000;
set hive.exec.reducers.max = 300;

--在map中会做部分聚集操作，效率更高但需要更多的内存
set hive.map.aggr=true;
--这个是group的键对应的记录条数超过这个值则会进行分拆,值根据具体数据量设置  在Map端进行聚合操作的条目数目
set hive.groupby.mapaggr.checkinterval=100000;
--如果是group by过程出现倾斜 应该设置为true
set hive.groupby.skewindata=true;
--这个是join的键对应的记录条数超过这个值则会进行分拆,值根据具体数据量设置
set hive.skewjoin.key=100000; 
--如果是join 过程出现倾斜 应该设置为true
set hive.optimize.skewjoin=true;

动态分区设置
set hive.exec.dynamic.partition=true;
set hive.exec.dynamic.partition.mode=nonstrict;
set hive.optimize.sort.dynamic.partition = false;

多列转一行     select  id, concat_ws(',',collect_set(name)) from test group by id;--

时间控制：
日期格式转换          SELECT date_format('2015-04-08', 'yyyyMMdd');--20150408
时间字符串转毫秒值    SELECT unix_timestamp('2016-04-08', 'yyyy-MM-dd');--1460041200 （无参为生成当前时间戳毫秒值）
毫秒值转时间字符串    SELECT from_unixtime(1460041200,'yyyyMMdd')--'20160408'
    日期对应星期          SELECT pmod(datediff(from_unixtime(unix_timestamp('20190127','yyyyMMdd'),'yyyy-MM-dd'), '2019-01-21') - 10, 7)

regexp_replace(xxx,'\t|\n|\r|','')  去除引起异常字符

生成随机UUID          SELECT reflect('java.util.UUID', 'randomUUID');  
正则like              rlike '正则'  
正则选取              SELECT regexp_extract('100-200', '(\d+)-(\d+)', 1); --100
正则替换              SELECT regexp_replace('100-200', '(\d+)', 'num');--num-num
分区排序自增字段      row_number() over (partition by order by )
左右补齐              SELECT rpad('hi', 5, '??')--hi???;  SELECT rpad('hi', 1, '??')--h;
集合排序              SELECT sort_array(array('b', 'd', 'c', 'a'), true);-- ["a","b","c","d"]
字符串转map           SELECT str_to_map('a:1,b:2,c:3', ',', ':');--map("a":"1","b":"2","c":"3")
字符串填充            SELECT format_string("Hello World %d %s", 100, "days");--Hello World 100 days
base64解码转utf8      decode(unbase64(bidword),'UTF-8')
字符串是否含有元素    instr(error_info,'12117') >0
自动补齐位数		  lpad('cid',8,'0'); --'00000cid'

URL解析
    * parse_url('http://facebook.com/path/p1.php?query=1', 'HOST')返回'facebook.com' 
    * parse_url('http://facebook.com/path/p1.php?query=1', 'PATH')返回'/path/p1.php' 
    * parse_url('http://facebook.com/path/p1.php?query=1', 'QUERY')返回'query=1'，
    可以指定key来返回特定参数，例如
    * parse_url('http://facebook.com/path/p1.php?query=1', 'QUERY','query')返回'1'，
    * parse_url('http://facebook.com/path/p1.php?query=1#Ref', 'REF')返回'Ref' 
    * parse_url('http://facebook.com/path/p1.php?query=1#Ref', 'PROTOCOL')返回'http'


调用java函数
select reflect("java.lang.Math","max",column1,column2) from test_udf
使用案例3：使用apache commons中的函数，commons下的jar已经包含在hadoop的classpath中，所以可以直接使用。
使用方式如下：
select reflect("org.apache.commons.lang.math.NumberUtils","isNumber","123") from dual;
reflect("java.lang.String", "valueOf", 1)
使用案例4：使用第三方jar
add jar hdfs://hdpnn:9000/group/analysts/ide_taobao-hz_boqian.zwq/hive/udf_jar/11354193197093.jar;
select reflect(class_name,method_name,"123") from dual;

表生成+侧视图
select t.a,t1.b from (select '1' a,array('1','2') b) t lateral view explode(b) t1 as b;

hive -hivevar statis_date="20190126" -hivevar yes_date="20190125" -f t_aipl_user_behavior_mode_detail.sql

add jar /home/bigdata/software/hive/ext-lib/json-serde.jar;

load data local inpath '/home/hadoop/Desktop/data' overwrite into table t1;
delete from 表名 where statis_date =20190505;
truncate table [表名];

row format delimited fields terminated by '\t'

ROW FORMAT SERDE 
  'org.apache.hadoop.hive.ql.io.orc.OrcSerde' 
STORED AS INPUTFORMAT 
  'org.apache.hadoop.hive.ql.io.orc.OrcInputFormat' 
OUTPUTFORMAT 
  'org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat'

临时表指定orc： create table xxx STORED AS ORC as 

alter table aps.t_aipl_flx_view_base_d  DROP IF EXISTS PARTITION (statis_date='${statis_date}');
hive -hivevar statis_date="20190126" -hivevar yes_date="20190125" -f xxx.sql

修改表名
ALTER TABLE table_name RENAME TO new_table_name;
修改hive表注释
ALTER TABLE 表名 SET TBLPROPERTIES('comment' = '表注释内容');
修改hive表字段注释
ALTER TABLE 表名 CHANGE 列名 新的列名 新列名类型 COMMENT '列注释' [FIRST|AFTER column_name];
增加/更新列
ALTER TABLE table_name ADD|REPLACE COLUMNS (col_name data_type [CONMMENT col_comment], ...);
修改表文件格式和组织
ALTER TABLE table_name SET FILEFORMAT ORC;
ALTER TABLE table_name CLUSTERED BY (col_name, col_name, ...) [SORTED By (col_name, ...)] INTO num_buckets BUCKETS;

新增列，并指定顺序
alter table ** add columns (amount double);
alter table **  change amount amount double after catch_time ;


hive> create table bucketed_user (id int,name string)
> clustered by (id) sorted by (id asc) into 4 buckets;
重点1：CLUSTERED BY来指定划分桶所用列和划分桶的个数。HIVE对key的hash值除bucket个数取余数，保证数据均匀随机分布在所有bucket里。
重点2:SORTED BY对桶中的一个或多个列另外排序

动态分区设置
set hive.exec.dynamic.partition=true;
set hive.exec.dynamic.partition.mode=nonstrict;
Set hive.optimize.sort.dynamic.partition = false;


删库跑路 
drop table if exists table_name cascade;

hadoop fs -rm [-f] [-r |-R] [-skipTrash] URI [URI …]
-f:如果文件不存在不会显示错误等提示信息 
-R/-r:等价：递归删除子目录 
-skipTrash:清理垃圾文件，对于从超过文件容量的目录中删除文件很有用


1、合并输入文件(控制map数)
set mapred.job.name = xxx;
set mapred.max.split.size=256000000;#(默认值256000000)每个Map最大输入大小
set mapred.min.split.size.per.node=100000000;#(默认值1)一个节点上split的至少的大小(不常用)
set mapred.min.split.size.per.rack=100000000;#(默认值1)一个交换机下split的至少的大小
set hive.input.format=org.apache.hadoop.hive.ql.io.CombineHiveInputFormat;#(默认)执行Map前进行小文件合并
开启org.apache.hadoop.hive.ql.io.CombineHiveInputFormat后，一个data node节点上多个小文件会进行合并，合并文件数由mapred.max.split.size限制的大小决定
  ，mapred.min.split.size.per.node决定了多个data node上的文件是否需要合并，mapred.min.split.size.per.rack决定了多个交换机上的文件是否需要合并


2、合并输出文件
set hive.merge.mapfiles = true #(默认true)在Map-only的任务结束时合并小文件
set hive.merge.mapredfiles = true #(默认flase)在Map-Reduce的任务结束时合并小文件
set hive.merge.size.per.task = 256*1000*1000 #(默认)合并文件的大小
set hive.merge.smallfiles.avgsize=16000000 #(默认)当输出文件的平均大小小于该值时，启动一个独立的map-reduce任务进行文件merge。


3,控制reduce任务数
set hive.exec.reducers.bytes.per.reducer=1000000000;#(默认1G)reduce个数N=min(.reducers.max,inputFileSize/bytes.per.reducer)
set hive.exec.reducers.max=256;#(默认999)reduce任务最大不可超过999个
set mapred.reduce.tasks=10;#(默认值-1)直接指定reduce任务数


4,控制Hive中Map和reduce的数量
Hive中的sql查询会生成执行计划，执行计划以MapReduce的方式执行，那么结合数据和集群的大小，map和reduce的数量就会影响到sql执行的效率。
除了要控制Hive生成的Job的数量，也要控制map和reduce的数量。
1、 map的数量，通常情况下和split的大小有关系，之前写的一篇blog“map和reduce的数量是如何定义的”有描述。
 hive中默认的hive.input.format是org.apache.hadoop.hive.ql.io.CombineHiveInputFormat，对于combineHiveInputFormat,它的输入的map数量
由三个配置决定，
mapred.max.split.size 一个split最大的大小
mapred.min.split.size.per.node， 一个节点上split的至少的大小
mapred.min.split.size.per.rack 一个交换机下split至少的大小
它的主要思路是把输入目录下的大文件分成多个map的输入, 并合并小文件, 做为一个map的输入. 具体的原理是下述三步:
a、根据输入目录下的每个文件,如果其长度超过mapred.max.split.size,以block为单位分成多个split(一个split是一个map的输入),每个split的长度都大于mapred.max.split.size, 因为以block为单位, 因此也会大于blockSize, 此文件剩下的长度如果大于mapred.min.split.size.per.node, 则生成一个split, 否则先暂时保留.
b、现在剩下的都是一些长度效短的碎片,把每个rack下碎片合并, 只要长度超过mapred.max.split.size就合并成一个split, 最后如果剩下的碎片比mapred.min.split.size.per.rack大, 就合并成一个split, 否则暂时保留.
c、把不同rack下的碎片合并, 只要长度超过mapred.max.split.size就合并成一个split, 剩下的碎片无论长度, 合并成一个split.
举例: mapred.max.split.size=1000
mapred.min.split.size.per.node=300
mapred.min.split.size.per.rack=100
输入目录下五个文件,rack1下三个文件,长度为2050,1499,10, rack2下两个文件,长度为1010,80. 另外blockSize为500.
经过第一步, 生成五个split: 1000,1000,1000,499,1000. 剩下的碎片为rack1下:50,10; rack2下10:80
由于两个rack下的碎片和都不超过100, 所以经过第二步, split和碎片都没有变化.
第三步,合并四个碎片成一个split, 长度为150.
如果要减少map数量, 可以调大mapred.max.split.size, 否则调小即可.
其特点是: 一个块至多作为一个map的输入，一个文件可能有多个块，一个文件可能因为块多分给做为不同map的输入， 一个map可能处理多个块，可能处理多个文件。
2、 reduce数量
可以在hive运行sql的时，打印出来，如下：
Number of reduce tasks not specified. Estimated from input data size: 1
In order to change the average load for a reducer (in bytes):
  set hive.exec.reducers.bytes.per.reducer=<number>
In order to limit the maximum number of reducers:
  set hive.exec.reducers.max=<number>
In order to set a constant number of reducers:
  set mapred.reduce.tasks=<number>
reduce数量由以下三个参数决定，
mapred.reduce.tasks(强制指定reduce的任务数量)
hive.exec.reducers.bytes.per.reducer（每个reduce任务处理的数据量，默认为1000^3=1G）
hive.exec.reducers.max（每个任务最大的reduce数，默认为999）
计算reducer数的公式很简单N=min( hive.exec.reducers.max ，总输入数据量/ hive.exec.reducers.bytes.per.reducer )
  只有一个reduce的场景：
  a、没有group by 的汇总
  b、order by
  c、笛卡尔积
  

5,配置本地化(同时满足如下四项才会启用)
set hive.exec.mode.local.auto=true;#(默认false
set hive.exec.mode.local.auto.inputbytes.max=134217728;#(默认值128M)
set hive.exec.mode.local.auto.tasks.max=4;#(jobmap任务数默认4)
job的reduce数必须是0或1

set hive.mapred.local.mem=0;#(本地模式时map/reduce的内存使用量,默认0,无限制)
set hive.mapjoin.localtask.max.memory.usage=0.9;#(默认值)本地任务可以使用内存的百分比



6,配置map端join(不支持Full/RightOuterJoin)
1.自动转换为map端join(小表在左)
set hive.auto.convert.join=true;#(默认值true)是否自动转化为mapjoin
set hive.mapjoin.smalltable.filesize=25000000;#小表的最大文件大小默认25M
set hive.auto.convert.join.noconditionaltask=true;#(默认)是否将多个mapreduce合并为一个
set hive.auto.convert.join.noconditionaltask.size=10000000;#多个mapjoin转化为一个时,所有小表的文件大小总和的最大值
set hive.mapjoin.followby.gby.localtask.max.memory.usage=0.55;#(默认值)mapjoin在做groupby操作时,可以使用多大的内存来存储数据,太大则不会保存在内存里
2,使用/*+ MAPJOIN(time_dim, date_dim) */显示开启map端join

举例:
select /*+ MAPJOIN(time_dim, date_dim) */ count(*) from
store_sales 
join time_dim on (ss_sold_time_sk = t_time_sk) 
join date_dim on (ss_sold_date_sk = d_date_sk)
where t_hour = 8 and d_year = 2002
如果表time_dim和date_dim的大小符合上面参数配置的值，两个连接转换为map连接。
如果两个表大小之和符合上面参数的值，连个map连接合并为一个map连接。这减少了MR作业的数量，
并显著地增加了该查询的速度。

mapjoin优点:
1.不消耗集群资源的reduce资源
2.减少了reduce操作,加快了程序执行
3.降低网络负载

缺点:
1.占用内存(所以加载到内存中的表不能过大,因为每个计算节点都会加载一次)
2.生成较多的小文件

mapjoin因内存不足造成的MapredLocalTask进程异常解决方案:
1.set hive.auto.convert.join=false;关闭mapjoin
2.调小hive.smalltable.filesize,默认25M
3.hive.mapjoin.localtask.max.memory.usage调大到0.999
4.set hive.ignore.mapjoin.hint=true;(默认true,忽略mapjoin hint)
5.set mapred.child.java.opts=-Xmx2048m 调大java进程内存


7.开启并行执行
set hive.exec.parallel=true;#(默认值true)
set hive.exec.parallel.thread.number=8;(默认8)
一个sql存在两个及以上union all时将同时执行(即没有依赖关系的stage将同时执行)


8.reducejoin优化
1.在编写带有 join 操作的代码语句时，应该将条目少的表/子查询放在 Join 操作符的左边。 
因为在 Reduce 阶段，位于 Join 操作符左边的表的内容会被加载进内存，载入条目较少的表 
可以有效减少 OOM（out of memory）即内存溢出。所以对于同一个 key 来说，对应的 value 
值小的放前，大的放后，这便是“小表放前”原则(可以使用/*streamtable(table_name)*/来显式标记大表,这样大表放在前后都行)

2.如果 Join 的 key 相同，不管有多少个表，都会则会合并为一个 Map-Reduce
一个 Map-Reduce 任务，而不是 ‘n’个在做 OUTER JOIN 的时候也是一样

比如查询：
INSERT OVERWRITE TABLE pv_users 
SELECT pv.pageid, u.age FROM page_view p 
JOIN user u ON (pv.userid = u.userid) 
JOIN newuser x ON (u.userid = x.userid); 

如果 Join 的条件不相同，比如： 
INSERT OVERWRITE TABLE pv_users 
SELECT pv.pageid, u.age FROM page_view p 
JOIN user u ON (pv.userid = u.userid) 
JOIN newuser x on (u.age = x.age);   
Map-Reduce 的任务数目和 Join 操作的数目是对应的 

3.set hive.join.cache.size;#(默认值25000行)在做表的join时缓存在内存中的行数



9.insert+union all的优化
如果union all的部分个数大于2，或者每个union部分数据量大，应该拆成多个insert into 语句，实际测试过程中，执行时间能提升50%
insert overwite table tablename partition (dt= ....)
select ..... from (
select ... from A
union all
select ... from B
union all
select ... from C
) R

insert into table tablename partition (dt= ....)
select .... from A
WHERE ...;
insert into table tablename partition (dt= ....)
select .... from B
WHERE ...;
insert into table tablename partition (dt= ....)
select .... from C
WHERE ...;


10.进行group或join操作时数据倾斜的处理
set mapred.reduce.tasks= 200;或set hive.exec.reducers.bytes.per.reducer=1000000000---增大Reduce个数
set hive.groupby.mapaggr.checkinterval=100000 ;--这个是group的键对应的同样记录条数超过这个值会认为该key是倾斜的groupkey则会进行分拆,值根据具体数据量设置
set hive.groupby.skewindata=true; --如果是group by过程出现倾斜 应该设置为true
set hive.skewjoin.key=100000; --这个是join的键对应的同样记录条数超过这个值会认为该key是倾斜的joinkey则会进行分拆,值根据具体数据量设置
set hive.optimize.skewjoin=true;--如果是join 过程出现倾斜 应该设置为true
hive.map.aggr = true(默认为true)在Map端做combiner,假如map各条数据基本上不一样, 聚合没什么意义，做combiner反而画蛇添足,
hive里也考虑的比较周到通过参数
hive.groupby.mapaggr.checkinterval = 100000 (默认)
hive.map.aggr.hash.min.reduction=0.5(默认),
预先取100000条数据聚合,如果聚合后的条数/100000>0.5，则不再聚合


11.join长尾
背景
SQL在Join执行阶段会将Join Key相同的数据分发到同一个执行Instance上处理。如果某个Key上的数据量比较多，
会导致该Instance执行时间比其他Instance执行时间长。其表现为：执行日志中该Join Task的大部分Instance都已执行完成，
但少数几个Instance一直处于执行中，这种现象称之为长尾

长尾类别&优化方法
小表长尾
Join倾斜时，如果某路输入比较小，可以采用Mapjoin避免倾斜。Mapjoin的原理是将Join操作提前到Map端执行，
这样可以避免因为分发Key不均匀导致数据倾斜。但是Mapjoin的使用有限制，必须是Join中的从表比较小才可用。
所谓从表，即Left Outer Join中的右表，或者Right Outer Join中的左表。

热点值长尾
如果是因为热点值导致长尾，并且Join的输入比较大无法用Mapjoin，可以先将热点Key取出，对于主表数据用热点Key切分成热点数据和非热点数据两部分分别处理，
最后合并。我们举一个电商的例子，假设我们需要计算所有商品的pv。我们有如下两张表
取出非热点值和商品(product) join 得到非热点商品的pv
取出热点值和商品(product) join 得到热点商品的pv


空值长尾
join时，假设左表(left_table)存在大量的空值，空值聚集到一个reduce上。由于left_table 存在大量的记录，无法使用mapjoin 。
此时可以使用 coalesce(left_table.key, rand()*9999)将key为空的情况下赋予随机值，来避免空值集中造成长尾。
或者为空的不参与关联，子查询过滤 null,然后进行union all操作
SELECT * FROM log a 
JOIN bmw_users b ON a.user_id IS NOT NULL AND a.user_id=b.user_id 
UNION All SELECT * FROM log a WHERE a.user_id IS NULL


不同数据类型关联产生的倾斜问题
解决方法：把数据类型转换成字符串类型
SELECT * FROM s8_log a LEFT OUTER 
JOIN r_auction_auctions b ON a.auction_id=CASE(b.auction_id AS STRING) 


map长尾
Map端读取数据时，由于文件大小分布不均匀，一些map任务读取并处理的数据特别多，一些map任务处理的数据特别少，
造成map端长尾。这种情形没有特别好的方法，只能调节splitsize来增加mapper数量，让数据分片更小，以期望获得更均匀的分配。

reduce长尾
由于Distinct操作的存在，数据无法在Map端的Shuffle阶段根据Group By先做一次combine聚合操作,减少传输的数据量，
而是将所有的数据都传输到Reduce端，当Key的数据分发不均匀时，就会导致Reduce端长尾，特别当多个Distinct同时出现在一段SQL代码中时，
数据会被分发多次，不仅会造成数据膨胀N倍，也会把长尾现象放大N倍

优化方式1.
只有一个distinct 的情况
原sql
SELECT D1
    , D2
    , COUNT(DISTINCT CASE 
        WHEN A IS NOT NULL THEN B
    END) AS B_distinct_cnt
FROM xxx
GROUP BY D1,D2
   

改后的sql
create table tmp1
as
select D1,D2
count( case when A is not null then B end ) as B_cnt
from xxx
group by D1, D1

select D1,D2,
sum(case when B_cnt > 0 then 1 else 0 end) as B_distinct_cnt
from tmp1
group by D1,D2


多个distinct的情况
原始sql
select D1,D2,
count(distinct case when A is not null then B end) as B_distinct_cnt ,
count(distinct case when E is not null then C end) as C_distinct_cnt 
from xxx group by D1,D2

修改后的sql
create table tmp1
as
select D1,D2,
count( case when A is not null then B end ) as B_cnt
from xxx
group by D1, D1

create table tmp1_1
as
select D1,D2,
sum(case when B_cnt > 0 then 1 else 0 end) as B_distinct_cnt
from tmp1
group by D1,D2

create table tmp2
as
select D1,D2
count( case when E is not null then C end ) as C_cnt
from xxx
group by D1, D1

create table tmp2_1
as
select D1,D2,
sum(case when C_cnt > 0 then 1 else 0 end) as C_distinct_cnt
from tmp1
group by D1,D2

select 
t1.D1,t1.D2,
t1.B_distinct_cnt,
t2.C_distinct_cnt
from tmp1_1 t1
left outer join tmp2_1 t2
on t1.D1=t2.D1 and t1.D2=t2.D2

优化2.
尝试使用set mapred.reduce.tasks=10增大reducetask数,但hive在进行count这种全聚合运算时会忽略此参数而强制使用1个reduce.
变通的方法:将count与distinct拆分,先进性去重再计数
举例:
原sql
select count(distinct id) from table_name where ....

优化后:
select count(*) from (select distinct id from table_name where ...) t1
优化后性能提升80%


优化3:使用group by 代替distinct
select count(distinct id) from table_name where ....
优化后:
select count(*) from (select id from table_name where ... group by id) t1
性能提升7倍
原因:使用count会将所有的待去重数据都shuffle到一个reduce里面,而使用group by 将会把数据分布到多个reduce,从而提高了效率
总结:直接使用distinct可读性好,数据量不大的话推荐使用,如果数据量太大了,性能受到影响了,再考虑优化

12.先聚合再join

两张表先join再聚合效率远没有先聚合后join高


13.in/exists的优化
LEFT SEMI JOIN 是 IN/EXISTS 子查询的一种更高效的实现。
Hive 当前没有实现 IN/EXISTS 子查询，所以你可以用 LEFT SEMI JOIN 重写你的子查询语句。
LEFT SEMI JOIN 的限制是， JOIN 子句中右边的表只能在ON子句中设置过滤条件，
在 WHERE 子句、SELECT 子句或其他地方过滤都不行。
举例:
SELECT a.key, a.value
FROM a
WHERE a.key in
(SELECT b.key
FROM B);

可以被重写为：
SELECT a.key, a.val
FROM a LEFT SEMI JOIN b on (a.key = b.key)


14.分区裁剪,列裁剪以及尽量早地过滤数据
尽量早的过滤数据,减少每个阶段的数据量,对于分区表要加分区,同时只选择需要使用的字段(分区裁剪,列裁剪)


15.order by,sort by ,distribute by ,cluster by 的使用场景

order by 为全局排序,只有一个reduce且无法配置,orderby 如果在strict 模式(hive.mapred.mode=strict,默认nonstrict)必须在语句中加上 limit关键字
sort by limit n 将会启动两个job,第一个job是每个reduce中做局部排序,然后分别取top n.第二个job再对分别局部排序好的数据做全局排序,从而大大提高select top n的效率(可单独使用,但通常与sort by结合使用)
distribute by 是控制map端如何拆分数据给reduce端(相当于mr中的partion操作),字段要选取非热点数据的列,否则容易造成数据倾斜.sort by与distribute by key相同时详相当于 cluster by
(在查询数据时可以使用distribute by,并且查询的字段要包含distribute by的字段)
cluster by 除了具有distribute by的功能外还兼具sort by的功能。但是排序只能是倒叙排序，不能指定排序规则为ASC或者DESC。

通过设置reduce的个数set mapred.reduce.tasks=2;以及使用distribute by 将表中的数据分成两份导出到本地文件夹中
hive (hive)> insert overwrite local directory '/usr/local/src/user.txt' select * from user distribute by id;
//MapReduce...
Hadoop job information for Stage-1: number of mappers: 1; number of reducers: 2


16.multi insert



17.是否存在多对多关联
只要遇到表关联就要调研一下,是否存在多对多关联,起码得保证有一个表或者结果集的关联键不重复.


18.limit优化快速出结果
一般情况下，Limit语句还是需要执行整个查询语句，然后再返回部分结果。
有一个配置属性可以开启，避免这种情况---对数据源进行抽样
hive.limit.optimize.enable=true --- 开启对数据源进行采样的功能
hive.limit.row.max.size=100000;#(默认值) --- 设置最小的采样容量
hive.limit.optimize.limit.file=10;#(默认值) --- 设置最大的采样样本数
缺点：有可能部分数据永远不会被处理到



19.严格模式
set hive.marped.mode=strict ------ 防止用户执行那些可能意想不到的不好的影响的查询
-- 分区表，必须选定分区范围
-- 对于使用order by的查询，要求必须使用limit语句。因为order by为了执行排序过程会将所有的结果数据分发到同一个reducer中进行处理。
-- 限制笛卡尔积查询：两张表join时必须有on语句


20.使用索引：
hive.optimize.index.filter=true;#(默认值false)自动使用索引
hive.optimize.index.groupby=true;#(默认false)使用聚合索引优化GROUP BY操作


21.推测执行：
mapred.map.tasks.speculative.execution=true;#(默认true)
mapred.reduce.tasks.speculative.execution=true;#(默认true)
hive.mapred.reduce.tasks.speculative.execution=true;#(默认true)
所谓的推测执行，就是当所有task都开始运行之后，Job Tracker会统计所有任务的平均进度，
如果某个task所在的task node机器配置比较低或者CPU load很高（原因很多），
导致任务执行比总体任务的平均执行要慢，此时Job Tracker会启动一个新的任务（duplicate task），
然后原有任务和新任务哪个先执行完就把另外一个kill掉，这也是我们经常在Job Tracker页面看到任务执行成功，
但是总有些任务被kill，就是这个原因。
有以下几种情况最好是将那个参数设为false：

1.当执行的任务相比集群其他任务执行时间非常长，占用的slot数很多。
这种情况下会推断为任务执行的较慢，会启动更多的task，而slot资源本身就非常的紧张，又翻倍往上长，集群上的其他任务就根本抢不到slot了，对集群是危害很大的事情。

2.当任务是操作redis，hbase这种中间存储层的时候。
像对这些中间存储层进行读写操作的时候，它们本身就承担着压力，推断执行一开，我勒个去了，压力就要翻倍，这对于性能是一件非常不好的事情。



22.综合性能提升配置(1Tdata)
set mapred.max.split.size=134217728 ;#(128M)
set hive.auto.convert.join=True;#(默认true)
set hive.auto.convert.join.noconditionaltask=True;#(默认true)
set hive.auto.convert.join.noconditionaltask.size=100000000;
set hive.groupby.skewindata=false#(默认false)开启时在处理非数据倾斜时性能显著下降
set hive.exec.parallel=True;#(默认true)
set hive.exec.parallel.thread.number=8;#(默认值8)

set hive.groupby.skewindata=false;#(默认false)
当启用时，能够解决数据倾斜的问题，但如果要在查询语句中对多个字段进行去重统计时会报错。
hive> set hive.groupby.skewindata=true;
hive> select count(distinct id),count(distinct x) from test;
FAILED: SemanticException [Error 10022]: DISTINCT on different columns not supported with skew in data
下面这种方式是可以正常查询
hive>select count(distinct id, x) from test; 


23.优化总结
优化时，把hive sql当做mapreduce程序来读，会有意想不到的惊喜。理解hadoop的核心能力，是hive优化的根本。这是这一年来，项目组所有成员宝贵的经验总结。

1.长期观察hadoop处理数据的过程，有几个显著的特征:
不怕数据多，就怕数据倾斜。
对jobs数比较多的作业运行效率相对比较低，比如即使有几百行的表，如果多次关联多次汇总，产生十几个jobs，没半小时是跑不完的。map reduce作业初始化的时间是比较长的。
对sum，count来说，不存在数据倾斜问题。(在map阶段通过combine操作汇总过数据,减少了分发到reduce的数据量)
对count(distinct ),效率较低，数据量一多，准出问题，如果是多count(distinct )效率更低。
2.优化可以从几个方面着手：
好的模型设计事半功倍。
解决数据倾斜问题。
减少job数。
设置合理的map reduce的task数，能有效提升性能。(比如，10w+级别的计算，用160个reduce，那是相当的浪费，1个足够)。
自己动手写sql解决数据倾斜问题是个不错的选择。set hive.groupby.skewindata=true;这是通用的算法优化，但算法优化总是漠视业务，习惯性提供通用的解决方法。 Etl开发人员更了解业务，更了解数据，所以通过业务逻辑解决倾斜的方法往往更精确，更有效。
对count(distinct)采取漠视的方法，尤其数据大的时候很容易产生倾斜问题，不抱侥幸心理。自己动手，丰衣足食。
对小文件进行合并，是行至有效的提高调度效率的方法，假如我们的作业设置合理的文件数，对云梯的整体调度效率也会产生积极的影响。
优化时把握整体，单个作业最优不如整体最优.




