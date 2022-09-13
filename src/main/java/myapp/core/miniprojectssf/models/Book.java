package myapp.core.miniprojectssf.models;

import java.io.StringReader;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class Book {

  private Integer rank;
  private String primary_isbn13;
  private String publisher;
  private String description;
  private String title;
  private String author;
  private String book_image;
  private String amazon_product_url;

  public Integer getRank() {
    return rank;
  }

  public void setRank(Integer rank) {
    this.rank = rank;
  }

  public String getPrimary_isbn13() {
    return primary_isbn13;
  }

  public void setPrimary_isbn13(String primary_isbn13) {
    this.primary_isbn13 = primary_isbn13;
  }

  public String getPublisher() {
    return publisher;
  }

  public void setPublisher(String publisher) {
    this.publisher = publisher;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getBook_image() {
    return book_image;
  }

  public void setBook_image(String book_image) {
    this.book_image = book_image;
  }

  public String getAmazon_product_url() {
    return amazon_product_url;
  }

  public void setAmazon_product_url(String amazon_product_url) {
    this.amazon_product_url = amazon_product_url;
  }

  public static Book create(JsonObject json) {
    final Book book = new Book();
    book.setPrimary_isbn13(json.getString("primary_isbn13"));
    book.setRank(json.getJsonNumber("rank").intValue());
    book.setTitle(json.getString("title"));
    book.setAuthor(json.getString("author"));
    book.setPublisher(json.getString("publisher"));
    book.setDescription(json.getString("description"));
    book.setAmazon_product_url(json.getString("amazon_product_url"));
    book.setBook_image(json.getString("book_image"));

    return book;
  }

  public static Book create(String json) {
    try (StringReader strReader = new StringReader(json)) {
      JsonReader j = Json.createReader(strReader);
      return create(j.readObject());
    }
  }

  public JsonObject toJson() {
    return Json.createObjectBuilder()
        .add("primary_isbn13", this.primary_isbn13)
        .add("rank", this.rank)
        .add("title", this.title)
        .add("author", this.author)
        .add("publisher", this.publisher)
        .add("description", this.description)
        .add("amazon_product_url", this.amazon_product_url)
        .add("book_image", this.book_image)
        .build();
  }

  @Override
  public String toString() {
    return "Book [primary_isbn13=" + primary_isbn13 + ", title=" + title + ", author="
        + author + ", book_image=" + book_image
        + ", description=" + description + ", publisher=" + publisher + ", rank="
        + rank + ", amazon_product_url=" + amazon_product_url + "]";
  }

}
