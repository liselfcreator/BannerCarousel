package com.liselfcreator.mediapermissiondevapplication.myapplication

import com.liselfcreator.bannercarousel.BannerModel
import com.liselfcreator.bannercarousel.BannerStatus

object Data {
    fun getData(): List<BannerModel> {
        return listOf(
            BannerModel(
                imgUrl = "https://cover.novelflow.app/prod/img/cover/a094e3c7291c406eb5defd4b14d1f5fd.jpg",
                status = BannerStatus.COMPLETED,
                score = "4.8",
                views = "12.4k"
            ),
            BannerModel(
                imgUrl = "https://cover.novelflow.app/prod/img/cover/a7c0fe59a62c4e56a7f124f0da812952.jpg",
                status = BannerStatus.ONGOING,
                score = "4.5",
                views = "40.7k"
            ),
            BannerModel(
                imgUrl = "https://cover.novelflow.app/prod/img/cover/f6b52ef7f2b14d649ce5e42796d65dc9.jpg",
                status = BannerStatus.COMPLETED,
                score = "4.9",
                views = "23.1k"
            ),
            BannerModel(
                imgUrl = "https://cover.novelflow.app/prod/img/cover/f725aa90f59b4424a0f3384bda2636ac.jpg",
                status = BannerStatus.ONGOING,
                score = "4.4",
                views = "18.0k"
            ),
            BannerModel(
                imgUrl = "https://cover.novelflow.app/prod/img/cover/b2ce02419e764e0997a64aa57adb9d0f.jpg",
                status = BannerStatus.COMPLETED,
                score = "4.7",
                views = "8.6k"
            ),
            BannerModel(
                imgUrl = "https://cover.novelflow.app/prod/img/cover/3f232bb8462949308a07ca413d552243.jpg",
                status = BannerStatus.ONGOING,
                score = "4.3",
                views = "32.0k"
            )
        )
    }
}
