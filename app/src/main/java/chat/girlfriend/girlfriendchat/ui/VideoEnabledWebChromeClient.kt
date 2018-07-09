package chat.girlfriend.girlfriendchat.ui

import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.media.MediaPlayer.OnErrorListener
import android.media.MediaPlayer.OnPreparedListener
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.webkit.WebChromeClient
import android.widget.FrameLayout
import android.widget.VideoView

/**
 * This class serves as a WebChromeClient to be set to a WebView, allowing it to play video.
 * It works with old VideoView-s as well as the new HTML5VideoFullScreen inner classes.
 * With VideoView (typically API level <11), it will always show full-screen.
 * With HTML5VideoFullScreen (typically API level 11+), it will show both in-page and full-screen.
 *
 * IMPORTANT NOTES:
 * - For API level 11+, android:hardwareAccelerated="true" must be set in the application manifest.
 * - The invoking activity must call VideoEnabledWebChromeClient's onBackPressed() inside of its own onBackPressed().
 * - Tested in Android API level 8+. Only tested on http://m.youtube.com.
 *
 * @author Cristian Perez (http://cpr.name)
 */
open class VideoEnabledWebChromeClient : WebChromeClient, OnPreparedListener, OnCompletionListener, OnErrorListener {

    private var activityNonVideoView: View? = null
    private var activityVideoView: ViewGroup? = null
    private var loadingView: View? = null
    private var webView: VideoEnabledWebView? = null

    /**
     * Indicates if the video is being displayed using a custom view (typically full-screen)
     * @return true it the video is being displayed using a custom view (typically full-screen)
     */
    var isVideoFullscreen: Boolean = false
        private set // Indicates if the video is being displayed using a custom view (typically full-screen)
    private var videoViewContainer: FrameLayout? = null
    private var videoViewCallback: WebChromeClient.CustomViewCallback? = null

    private var toggledFullscreenCallback: ToggledFullscreenCallback? = null

    interface ToggledFullscreenCallback {
        fun toggledFullscreen(fullscreen: Boolean)
    }

    /**
     * Never use this constructor alone.
     * This constructor allows this class to be defined as an inline inner class in which the user can override methods
     */
    constructor() {}

    /**
     * Builds a video enabled WebChromeClient.
     * @param activityNonVideoView A View in the activity's layout that contains every other view that should be hidden when the video goes full-screen.
     * @param activityVideoView A ViewGroup in the activity's layout that will display the video. Typically you would like this to fill the whole layout.
     */
    constructor(activityNonVideoView: View, activityVideoView: ViewGroup) {
        this.activityNonVideoView = activityNonVideoView
        this.activityVideoView = activityVideoView
        this.loadingView = null
        this.webView = null
        this.isVideoFullscreen = false
    }

    /**
     * Builds a video enabled WebChromeClient.
     * @param activityNonVideoView A View in the activity's layout that contains every other view that should be hidden when the video goes full-screen.
     * @param activityVideoView A ViewGroup in the activity's layout that will display the video. Typically you would like this to fill the whole layout.
     * @param loadingView A View to be shown while the video is loading (typically only used in API level <11). Must be already inflated and without a parent view.
     */
    constructor(activityNonVideoView: View, activityVideoView: ViewGroup, loadingView: View) {
        this.activityNonVideoView = activityNonVideoView
        this.activityVideoView = activityVideoView
        this.loadingView = loadingView
        this.webView = null
        this.isVideoFullscreen = false
    }

    /**
     * Builds a video enabled WebChromeClient.
     * @param activityNonVideoView A View in the activity's layout that contains every other view that should be hidden when the video goes full-screen.
     * @param activityVideoView A ViewGroup in the activity's layout that will display the video. Typically you would like this to fill the whole layout.
     * @param loadingView A View to be shown while the video is loading (typically only used in API level <11). Must be already inflated and without a parent view.
     * @param webView The owner VideoEnabledWebView. Passing it will enable the VideoEnabledWebChromeClient to detect the HTML5 video ended event and exit full-screen.
     * Note: The web page must only contain one video tag in order for the HTML5 video ended event to work. This could be improved if needed (see Javascript code).
     */
    constructor(activityNonVideoView: View, activityVideoView: ViewGroup, loadingView: View, webView: VideoEnabledWebView) {
        this.activityNonVideoView = activityNonVideoView
        this.activityVideoView = activityVideoView
        this.loadingView = loadingView
        this.webView = webView
        this.isVideoFullscreen = false
    }

    /**
     * Set a callback that will be fired when the video starts or finishes displaying using a custom view (typically full-screen)
     * @param callback A VideoEnabledWebChromeClient.ToggledFullscreenCallback callback
     */
    fun setOnToggledFullscreen(callback: ToggledFullscreenCallback) {
        this.toggledFullscreenCallback = callback
    }

    override fun onShowCustomView(view: View, callback: WebChromeClient.CustomViewCallback) {
        if (view is FrameLayout) {
            // A video wants to be shown
            val focusedChild = view.focusedChild

            // Save video related variables
            this.isVideoFullscreen = true
            this.videoViewContainer = view
            this.videoViewCallback = callback

            // Hide the non-video view, add the video view, and show it
            activityNonVideoView!!.visibility = View.INVISIBLE
            activityVideoView!!.addView(videoViewContainer, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
            activityVideoView!!.visibility = View.VISIBLE

            if (focusedChild is VideoView) {
                // VideoView (typically API level <11)
                // Handle all the required events
                focusedChild.setOnPreparedListener(this)
                focusedChild.setOnCompletionListener(this)
                focusedChild.setOnErrorListener(this)
            } else
            // Usually android.webkit.HTML5VideoFullScreen$VideoSurfaceView, sometimes android.webkit.HTML5VideoFullScreen$VideoTextureView
            {
                // HTML5VideoFullScreen (typically API level 11+)
                // Handle HTML5 video ended event
                if (webView != null && webView!!.getSettings().getJavaScriptEnabled()) {
                    // Run javascript code that detects the video end and notifies the interface
                    var js = "javascript:"
                    js += "_ytrp_html5_video = document.getElementsByTagName('video')[0];"
                    js += "if (_ytrp_html5_video !== undefined) {"
                    run {
                        js += "function _ytrp_html5_video_ended() {"
                        run {
                            js += "_ytrp_html5_video.removeEventListener('ended', _ytrp_html5_video_ended);"
                            js += "_VideoEnabledWebView.notifyVideoEnd();" // Must match Javascript interface name and method of VideoEnableWebView
                        }
                        js += "}"
                        js += "_ytrp_html5_video.addEventListener('ended', _ytrp_html5_video_ended);"
                    }
                    js += "}"
                    webView!!.loadUrl(js)
                }
            }

            // Notify full-screen change
            if (toggledFullscreenCallback != null) {
                toggledFullscreenCallback!!.toggledFullscreen(true)
            }
        }
    }

    override fun onShowCustomView(view: View, requestedOrientation: Int, callback: WebChromeClient.CustomViewCallback) // Only available in API level 14+
    {
        onShowCustomView(view, callback)
    }

    override fun onHideCustomView() {
        // This method must be manually (internally) called on video end in the case of VideoView (typically API level <11)
        // This method must be manually (internally) called on video end in the case of HTML5VideoFullScreen (typically API level 11+) because it's not always called automatically
        // This method must be manually (internally) called on back key press (from this class' onBackPressed() method)

        if (isVideoFullscreen) {
            // Hide the video view, remove it, and show the non-video view
            activityVideoView!!.visibility = View.INVISIBLE
            activityVideoView!!.removeView(videoViewContainer)
            activityNonVideoView!!.visibility = View.VISIBLE

            // Call back
            if (videoViewCallback != null) videoViewCallback!!.onCustomViewHidden()

            // Reset video related variables
            isVideoFullscreen = false
            videoViewContainer = null
            videoViewCallback = null

            // Notify full-screen change
            if (toggledFullscreenCallback != null) {
                toggledFullscreenCallback!!.toggledFullscreen(false)
            }
        }
    }

    override fun getVideoLoadingProgressView() // Video will start loading, only called in the case of VideoView (typically API level <11)
            : View? {
        if (loadingView != null) {
            loadingView!!.visibility = View.VISIBLE
            return loadingView
        } else {
            return super.getVideoLoadingProgressView()
        }
    }

    override fun onPrepared(mp: MediaPlayer) // Video will start playing, only called in the case of VideoView (typically API level <11)
    {
        if (loadingView != null) {
            loadingView!!.visibility = View.GONE
        }
    }

    override fun onCompletion(mp: MediaPlayer) // Video finished playing, only called in the case of VideoView (typically API level <11)
    {
        onHideCustomView()
    }

    override fun onError(mp: MediaPlayer, what: Int, extra: Int) // Error while playing video, only called in the case of VideoView (typically API level <11)
            : Boolean {
        return false // By returning false, onCompletion() will be called
    }

    /**
     * Notifies the class that the back key has been pressed by the user.
     * This must be called from the Activity's onBackPressed(), and if it returns false, the activity itself should handle it. Otherwise don't do anything.
     * @return Returns true if the event was handled, and false if it is not (video view is not visible)
     */
    fun onBackPressed(): Boolean {
        if (isVideoFullscreen) {
            onHideCustomView()
            return true
        } else {
            return false
        }
    }

}