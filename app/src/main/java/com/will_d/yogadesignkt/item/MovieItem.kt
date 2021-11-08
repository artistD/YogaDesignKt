package com.will_d.yogadesignkt.item

class MovieItem(var rank : String, var title : String, var openMovie:String, var audiSum : String) {

    var imgUrl :String? = null
    var subTitle:String? = null
    var director:String? = null
    var actor :String? = null
    var userRationg :String? = null

    constructor(rank: String, title: String, openMovie: String, audiSum: String, imgUrl : String, subTitle:String, director:String, actor:String, userRationg :String):this(rank, title, openMovie, audiSum){
        this.imgUrl = imgUrl
        this.subTitle = subTitle
        this.director = director
        this.actor = actor
        this.userRationg = userRationg

    }
}