package com.cia103g5.user.post.model;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;


@Embeddable
public class FavPostKey implements Serializable {

    @Column(name = "mem_id")
    private int memId;

    @Column(name = "post_no")
    private int postNo;

    // 預設構造函數（必要）
    public FavPostKey() {}

    // 帶參數的構造函數
    public FavPostKey(int memId, int postNo) {
        this.memId = memId;
        this.postNo = postNo;
    }

    // Getter 與 Setter 方法
    public int getMemId() {
        return memId;
    }

    public void setMemId(int memId) {
        this.memId = memId;
    }

    public int getPostNo() {
        return postNo;
    }

    public void setPostNo(int postNo) {
        this.postNo = postNo;
    }

    // equals 和 hashCode 方法
//    這段程式碼定義了 equals() 和 hashCode() 方法，並且是重寫（@Override）了這些方法。這通常是為了實現某個類的 "值比較" 和 "集合操作" 的行為。讓我逐一解釋它們的作用：
//
//    equals() 方法
//    equals() 方法用於判斷兩個物件是否「相等」。在 Java 中，預設的 equals() 方法比較的是物件的記憶體位址（參考），但有時候我們希望根據物件的屬性來比較它們是否相等。
//
//    在這段代碼中：
//    
//    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;// 如果比較的是自己，直接返回 true
        if (o == null || getClass() != o.getClass()) return false;// 如果傳入的物件為 null 或不是同一類型，返回 false
        FavPostKey that = (FavPostKey) o;// 將傳入的物件轉換為 FavPostKey 類型
        return memId == that.memId && postNo == that.postNo;// 比較兩個物件的 memId 和 postNo 屬性是否相等
    }

//    這段 equals() 方法用來比較 FavPostKey 物件是否相等，判斷條件是：
//
//    引用相同（this == o）：如果兩個變數引用同一個物件，那麼它們顯然是相等的，直接返回 true。
//    類型不同或為空（o == null || getClass() != o.getClass()）：如果比較的物件為 null 或類型不同，直接返回 false。
//    屬性比較（memId == that.memId && postNo == that.postNo）：如果類型相同，那麼進行屬性的逐一比較。只有當 memId 和 postNo 都相等時，才認為兩個物件相等。
    
    
    
//hashCode() 方法
//    hashCode() 方法返回一個整數，用來在基於雜湊的集合（如 HashSet, HashMap）
//    中使用時進行快速查找。當重寫 equals() 時，通常也需要重寫 hashCode()，
//    以確保相等的物件具有相同的雜湊碼。
    @Override
    public int hashCode() {
        return Objects.hash(memId, postNo);
    }
}

//hashCode() 方法利用 Objects.hash() 方法來生成 memId 和 postNo 屬性的雜湊值：
//
//Objects.hash(memId, postNo) 會根據 memId 和 postNo 的值來計算雜湊碼，這樣可以確保：
//如果兩個物件的 memId 和 postNo 相同，那麼它們的 hashCode() 也相同，這是符合集合中使用的原則。
//有助於在 HashSet 或 HashMap 等基於雜湊的集合中高效地插入、查找和刪除物件。
//為什麼要重寫 equals() 和 hashCode()
//值比較：重寫 equals() 方法是為了實現物件的「值相等」比較，而不只是比較記憶體位址。
//集合中的唯一性：在使用基於雜湊的集合（例如 HashSet 或 HashMap）時，重寫 hashCode() 會確保相等的物件具有相同的雜湊值，以便正確判斷集合中的唯一性。
//使用場景
//這段代碼最常見於實體類中有複合主鍵的情況，或在集合操作中需要依據物件的屬性判斷相等性時。例如，FavPostKey 可能是一個複合主鍵類，用於描述某個用戶對某篇文章的收藏行為，因此比較時需要考慮 memId 和 postNo 兩個字段。
//
//這樣定義的 equals() 和 hashCode() 方法可以保證相同的用戶和文章對應的 FavPostKey 是唯一的，避免重複記錄。
//
