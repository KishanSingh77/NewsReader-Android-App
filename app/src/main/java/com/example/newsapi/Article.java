package com.example.newsapi;

public class Article {

    public Source source ;
    public String author ;
    public  String title ;
    public  String description ;
    public   String url ;
    public  String urlToImage ;
    public String publishedAt ;
    public  String content ;

    @Override
    public String toString() {
        return "Article{" +
                "source=" + source +
                ", author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", url='" + url + '\'' +
                ", urlToImage='" + urlToImage + '\'' +
                ", publishedAt='" + publishedAt + '\'' +
                ", content='" + content + '\'' +
                '}';
    }






}


/*{
         "source": {
            "id": null,
            "name": "Zdnet.fr"
         },
         "author": "Stéphanie Chaptal",
         "title": "Quelles sont les utilisations pratiques de la blockchain ?",
         "description": "Popularisée par bitcoin et les autres cryptomonnaies, la blockchain ou technologie des chaines de blocs, s’insinue peu à peu dans tous les domaines pour de nouveaux usages. En voici 5 ayant dépassé le stade de simple prototype technique.",
         "url": "https://www.zdnet.fr/guide-achat/quelles-sont-les-utilisations-pratiques-de-la-blockchain-39864018.htm",
         "urlToImage": "https://d1fmx1rbmqrxrr.cloudfront.net/zdnet/i/edit/ne/2018/10/blockchain-dsi-620.jpg",
         "publishedAt": "2019-09-30T16:55:01Z",
         "content": "Apparue en pleine lumière avec lessor des cryptomonnaies et plus particulièrement de bitcoin, la blockchain ne se limite pas à léchange de devises.\r\n \r\nDéfinie par Cyril Grunspan, responsable pédagogique du département dIngénierie Financière de lESILV, la com… [+4205 chars]"
      }*/