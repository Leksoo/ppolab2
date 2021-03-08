package model

import org.bson.Document

data class User(
    val id: Int,
    val name: String,
    val currency: Currency
) {

    fun asDocument(): Document {
        return Document().append("id", id).append("name", name).append("currency", currency.name)
    }

    override fun toString(): String {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", currency='" + currency.name + '\'' +
                '}'
    }


    companion object {
        fun fromDocument(doc: Document) = User(
            doc.getInteger("id"),
            doc.getString("name"),
            Currency.valueOf(doc.getString("currency"))
        )
    }
}