package model

import org.bson.Document

data class Product(
    val name: String,
    val cost: Double
) {

    fun asDocument(): Document {
        return Document().append("name", name).append("cost", cost)
    }

    override fun toString(): String {
        return "Product{" +
                "name=" + name +
                ", cost='" + cost + '\'' +
                '}'
    }

    companion object {
        fun fromDocument(doc: Document) = Product(
            doc.getString("name"),
            doc.getDouble("cost")
        )
    }
}