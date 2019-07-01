package pers.harry.search;

import java.io.File;

import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;

public class SearchIndex {
    @Test
    public void search(){
        try {
            String keyword = "开发";
            // 在这里进行检索的时候，需要加载的目录就是创建索引的目录，创建索引以后，那些原数据源在Lucene上就暂时用不到了
            Directory directory = FSDirectory.open((new File("index1")).toPath());
            IndexReader reader = DirectoryReader.open(directory);
            // IndexSearcher 是Lucene的检索的入口点，所有检索都从这里入口
            IndexSearcher searcher = new IndexSearcher(reader);
            // 通过analyzer对用户输入的词进行分词
            SmartChineseAnalyzer analyzer = new SmartChineseAnalyzer();
            // 构建检索条件
            QueryParser parser = new QueryParser("title", analyzer);

            Query query = parser.parse(keyword);
            // 最后使用searcher.search检索，search方法的参数很多，还可以根据需求，取出相应的条数
            TopDocs topDocs = searcher.search(query, 20);
            // topDocs.totalHits 返回的是所有检索到记录的条数的总和
            ScoreDoc[] docs = topDocs.scoreDocs;
            System.out.println("Keyword \"" + keyword + "\" get " + topDocs.totalHits + " docs\n");
            for(ScoreDoc doc : docs){
                int docId = doc.doc;
                Document document = reader.document(docId);
                System.out.print("docId: " + docId);
                System.out.print(" title: " + document.get("title"));
                System.out.println(" content: " + document.get("content"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
