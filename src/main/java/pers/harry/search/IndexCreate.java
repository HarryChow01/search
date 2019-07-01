package pers.harry.search;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;

import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;


public class IndexCreate {
    @Test
    private static void analyzerTest() {
        Analyzer analyzer=new SmartChineseAnalyzer();
        TokenStream tokenStream = null;
        String str = "lucene将一篇文档分成若干个域，每个域又分成若干个词元，通过词元在文档中的重要程度，将文档转化为N维的空间向量";
        try {
            tokenStream = analyzer.tokenStream("myfiled", new StringReader(str));
            tokenStream.reset();
            while(tokenStream.incrementToken()){
                //取得下一个分词
                System.out.println(tokenStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
            analyzer.close();
        }
        analyzer.close();
    }

    public static void main(String[] args) {

        // 分词器SmartChineseAnalyzer
        Analyzer analyzer = new SimpleAnalyzer();
        //SmartChineseAnalyzer analyzer = new SmartChineseAnalyzer();

        //indexwriter 配置信息
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);

        //索引的打开方式，没有索引文件就新建，有就打开
        indexWriterConfig.setOpenMode(OpenMode.CREATE_OR_APPEND);
        Directory directory = null;
        IndexWriter indexWriter = null;
        try {
            //指定索引硬盘存储路径
            directory = FSDirectory.open((new File("index1")).toPath());
            System.out.println("Index dir: " + new File("index1").getAbsolutePath());
            //如果索引处于锁定状态，则解锁

            //指定所以操作对象indexWrite
            indexWriter = new IndexWriter(directory, indexWriterConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //创建文档一
        Document doc1 = new Document();
        //对name域赋值“测试标题”，存储域值信息
        doc1.add(new TextField("title", "案例 测试", Store.YES));
        //对content域赋值“测试标题”，存储域值信息
        doc1.add(new TextField("content", "案例 开发", Store.YES));
        try {
            //将文档写入到索引中
            indexWriter.addDocument(doc1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //创建文档二
        Document doc2 = new Document();
        doc2.add(new TextField("title", "案例 开发", Store.YES));
        doc2.add(new TextField("content", "通过词元在文档中的重要程度，将文档转化为空间向量", Store.YES));
        try {
            //将文档写入到索引中
            indexWriter.addDocument(doc2);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //将indexWrite操作提交，如果不提交，之前的操作将不会保存到硬盘
        try {
            //这一步很消耗系统资源，所以commit操作需要有一定的策略
            indexWriter.commit();
            //关闭资源
            indexWriter.close();
            directory.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

