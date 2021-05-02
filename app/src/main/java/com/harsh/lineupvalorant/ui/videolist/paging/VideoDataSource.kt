package com.harsh.lineupvalorant.ui.videolist.paging

/*

private const val VIDEO_LIST_INITIAL_PAGE_INDEX = 0

class VideoDataSource constructor(
    private val videoDao: VideoDao,
    private val query: String
) : PagingSource<Int, Video>() {


    override fun getRefreshKey(state: PagingState<Int, Video>): Int?  = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Video> {
        val position = params.key ?: VIDEO_LIST_INITIAL_PAGE_INDEX
        val videos = videoDao.getAllPagingVideos(query, params.loadSize)
        return try {
            LoadResult.Page(
                data = videos,
                prevKey = if (position == VIDEO_LIST_INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (videos.isNullOrEmpty()) null else position + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

}*/
