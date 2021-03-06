package com.wenhaiz.himusic.module.detail

import com.wenhaiz.himusic.MyApp
import com.wenhaiz.himusic.data.LoadAlbumDetailCallback
import com.wenhaiz.himusic.data.LoadCollectDetailCallback
import com.wenhaiz.himusic.data.LoadRankingDetailCallback
import com.wenhaiz.himusic.data.MusicRepository
import com.wenhaiz.himusic.data.bean.Album
import com.wenhaiz.himusic.data.bean.Collect
import com.wenhaiz.himusic.data.bean.Collect_
import com.wenhaiz.himusic.http.data.RankList

internal class DetailPresenter(val view: DetailContract.View) : DetailContract.Presenter {


    private val musicRepository: MusicRepository = MusicRepository.getInstance(view.getViewContext())

    init {
        view.setPresenter(this)
    }

    override fun loadAlbumDetail(album: Album) {
        musicRepository.loadAlbumDetail(album, object : LoadAlbumDetailCallback {
            override fun onStart() {
                view.onLoading()
            }

            override fun onFailure(msg: String) {
                view.onFailure(msg)
            }

            override fun onSuccess(album: Album) {
                view.onAlbumDetailLoad(album)
            }

        })
    }

    override fun loadCollectDetail(collect: Collect, isFromUser: Boolean) {
        if (isFromUser) {
            view.onLoading()
            val collectBox = MyApp.getBoxStore().boxFor(Collect::class.java)
            val findCollect: Collect? = collectBox.query().equal(Collect_.id, collect.id).build().findFirst()
            if (findCollect == null) {
                view.onFailure("没有找到该歌单信息！")
            } else {
                view.onCollectDetailLoad(collect)
            }

        } else {
            musicRepository.loadCollectDetail(collect, object : LoadCollectDetailCallback {
                override fun onStart() {
                    view.onLoading()
                }

                override fun onFailure(msg: String) {
                    view.onFailure(msg)
                }

                override fun onSuccess(collect: Collect) {
                    view.onCollectDetailLoad(collect)
                }

            })
        }

    }

    override fun loadRankingDetail(rank: RankList.Rank) {
        musicRepository.loadRankingDetail(rank, object : LoadRankingDetailCallback {
            override fun onSuccess(rank: RankList.Rank) {
                view.onRankingDetailLoad(rank)
            }

            override fun onStart() {
                view.onLoading()
            }

            override fun onFailure(msg: String) {
                view.onFailure(msg)
            }

        })
    }

    companion object {
        const val TAG = "DetailPresenter"
    }

}