package com.liselfcreator.mediapermissiondevapplication.myapplication

import com.liselfcreator.bannercarousel.BannerModel
import com.liselfcreator.bannercarousel.BannerStatus

object Data {
    fun getData(): List<BannerModel> {
        return listOf(
            BannerModel(
                imgUrl = "https://img.ixintu.com/upload/jpg/20210523/100af60244a0745748ad89d262ea0deb_215620_800_1354.jpg!ys",
                status = BannerStatus.COMPLETED,
                score = "4.8",
                views = "12.4k"
            ),
            BannerModel(
                imgUrl = "https://www.580dns.com/plugins/addons/wxinseo/template/picture/f32d9d08db514c4f3d46468f43ab02bf.jpg",
                status = BannerStatus.ONGOING,
                score = "4.5",
                views = "40.7k"
            ),
            BannerModel(
                imgUrl = "https://marketplace.canva.cn/EAGRk8Et7SY/1/0/1200w/canva-sMtK8YJkaVE.jpg",
                status = BannerStatus.COMPLETED,
                score = "4.9",
                views = "23.1k"
            ),
            BannerModel(
                imgUrl = "https://img2.tuguaishou.com/ips_templ_preview/31/c9/dc/lg_5861351_1762509914_690dc45aa591f.jpg!w300_new?auth_key=1762905600-0-0-2f3bc3a137a111703fe88cf7836f2ac6",
                status = BannerStatus.ONGOING,
                score = "4.4",
                views = "18.0k"
            ),
            BannerModel(
                imgUrl = "https://img.tuguaishou.com/ips_templ_preview/95/1a/18/lg_5868979_1762508723_690dbfb3b49ee.jpg!w300_new?auth_key=1762905600-0-0-3ced91a5b5e9d7145553c7edd5b9ec81",
                status = BannerStatus.COMPLETED,
                score = "4.7",
                views = "8.6k"
            ),
            BannerModel(
                imgUrl = "https://marketplace.canva.cn/EAGexd7yvTk/1/0/1200w/canva-46_giARy3dY.jpg",
                status = BannerStatus.ONGOING,
                score = "4.3",
                views = "32.0k"
            )
        )
    }
}
