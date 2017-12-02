package com.imangazaliev.notelin.mvp.model

import com.imangazaliev.notelin.utils.formatDate
import com.reactiveandroid.Model
import com.reactiveandroid.annotation.Column
import com.reactiveandroid.annotation.PrimaryKey
import com.reactiveandroid.annotation.Table
import java.util.*

@Table(name = "Notes", database = AppDatabase::class)
class Note : Model {

    @PrimaryKey
    var id: Long = 0
    @Column(name = "title")
    var title: String? = null
    @Column(name = "text")
    var text: String? = null
    @Column(name = "created_at")
    var createAt: Date? = null
    @Column(name = "change_at")
    var changedAt: Date? = null

    constructor(title: String, createDate: Date) {
        this.title = title
        this.createAt = createDate
        this.changedAt = createDate
    }

    constructor()

    fun getInfo(): String = "Title:\n$title\n" +
            "Created at:\n${formatDate(createAt)}\n" +
            "Changed at:\n${formatDate(changedAt)}"

}