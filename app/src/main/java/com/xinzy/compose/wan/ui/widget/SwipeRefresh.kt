package com.xinzy.compose.wan.ui.widget

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.MutatorMutex
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.xinzy.compose.wan.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.lang.Float.max
import java.lang.Float.min
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.math.pow
import kotlin.math.sin

enum class SwipeRefreshStyle {
    Translate,  //平移，即内容与Header一起向下滑动，Translate为默认样式
    FixedBehind, //固定在背后，即内容向下滑动，Header不动
    FixedFront, //固定在前面, 即Header固定在前，Header与Content都不滑动
    FixedContent //内容固定,Header向下滑动,即官方样式
}

@Stable
class SwipeRefreshState(
    isRefreshing: Boolean,
    refreshTrigger: Float,
    maxDrag: Float
) {
    private val _indicatorOffset = Animatable(0f)
    private val mutatorMutex = MutatorMutex()

    /**
     * 最大下拉距离
     */
    var maxDrag: Float by mutableFloatStateOf(maxDrag)
        internal set

    /**
     * 刷新生效距离
     */
    var refreshTrigger: Float by mutableFloatStateOf(refreshTrigger)
        internal set

    /**
     * Whether this [SwipeRefreshState] is currently refreshing or not.
     */
    var isRefreshing: Boolean by mutableStateOf(isRefreshing)
        internal set

    /**
     * Whether a swipe/drag is currently in progress.
     */
    var isSwipeInProgress: Boolean by mutableStateOf(false)
        internal set

    var headerState: RefreshHeaderState by mutableStateOf(RefreshHeaderState.PullDownToRefresh)
        internal set

    /**
     * The current offset for the indicator, in pixels.
     */
    val indicatorOffset: Float get() = _indicatorOffset.value

    internal suspend fun animateOffsetTo(offset: Float) {
        mutatorMutex.mutate {
            _indicatorOffset.animateTo(offset)
            updateHeaderState()
        }
    }

    /**
     * Dispatch scroll delta in pixels from touch events.
     */
    internal suspend fun dispatchScrollDelta(delta: Float) {
        mutatorMutex.mutate(MutatePriority.UserInput) {
            val slingShotOffset = getSlingShotOffset(_indicatorOffset.value + delta, maxDrag)
            _indicatorOffset.snapTo(slingShotOffset)
            updateHeaderState()
        }
    }

    private fun updateHeaderState() {
        headerState = if (isRefreshing) {
            RefreshHeaderState.Refreshing
        } else if (isSwipeInProgress) {
            if (indicatorOffset > refreshTrigger) {
                RefreshHeaderState.ReleaseToRefresh
            } else {
                RefreshHeaderState.PullDownToRefresh
            }
        } else {
            RefreshHeaderState.PullDownToRefresh
        }
    }

    private fun getSlingShotOffset(offsetY: Float, maxOffsetY: Float): Float {
        val offsetPercent = min(1f, offsetY / maxOffsetY)
        val extraOffset = abs(offsetY) - maxOffsetY

        // Can accommodate custom start and slingshot distance here
        val slingshotDistance = maxOffsetY
        val tensionSlingshotPercent = max(
            0f, min(extraOffset, slingshotDistance * 2) / slingshotDistance
        )
        val tensionPercent = ((tensionSlingshotPercent / 4) - (tensionSlingshotPercent / 4).pow(2)) * 2
        val extraMove = slingshotDistance * tensionPercent * 2
        return ((slingshotDistance * offsetPercent) + extraMove)
    }
}

@Composable
internal fun rememberSwipeRefreshState(
    isRefreshing: Boolean,
    refreshTrigger: Float,
    maxDrag: Float
): SwipeRefreshState {
    return remember {
        SwipeRefreshState(
            isRefreshing = isRefreshing,
            refreshTrigger = refreshTrigger,
            maxDrag = maxDrag
        )
    }.apply {
        this.isRefreshing = isRefreshing
        this.refreshTrigger = refreshTrigger
        this.maxDrag = maxDrag
    }
}

enum class RefreshHeaderState {
    PullDownToRefresh,
    Refreshing,
    ReleaseToRefresh
}


internal class SwipeRefreshNestedScrollConnection(
    private val state: SwipeRefreshState,
    private val coroutineScope: CoroutineScope,
    private val onRefresh: () -> Unit,
) : NestedScrollConnection {
    private val dragMultiplier = 0.5f

    var enabled: Boolean = false
    var refreshTrigger: Float = 0f

    override fun onPreScroll(
        available: Offset,
        source: NestedScrollSource
    ): Offset = when {
        // If swiping isn't enabled, return zero
        !enabled -> Offset.Zero
        // If we're refreshing, return zero
        state.isRefreshing -> Offset.Zero
        // If the user is swiping up, handle it
        source == NestedScrollSource.Drag && available.y < 0 -> onScroll(available)
        else -> Offset.Zero
    }

    override fun onPostScroll(
        consumed: Offset,
        available: Offset,
        source: NestedScrollSource
    ): Offset = when {
        // If swiping isn't enabled, return zero
        !enabled -> Offset.Zero
        // If we're refreshing, return zero
        state.isRefreshing -> Offset.Zero
        // If the user is swiping down and there's y remaining, handle it
        source == NestedScrollSource.Drag && available.y > 0 -> onScroll(available)
        else -> Offset.Zero
    }

    private fun onScroll(available: Offset): Offset {
        state.isSwipeInProgress = true

        val newOffset = (available.y * dragMultiplier + state.indicatorOffset).coerceAtLeast(0f)
        val dragConsumed = newOffset - state.indicatorOffset

        return if (dragConsumed.absoluteValue >= 0.5f) {
            coroutineScope.launch {
                state.dispatchScrollDelta(dragConsumed)
            }
            // Return the consumed Y
            Offset(x = 0f, y = dragConsumed / dragMultiplier)
        } else {
            Offset.Zero
        }
    }

    override suspend fun onPreFling(available: Velocity): Velocity {
        // If we're dragging, not currently refreshing and scrolled
        // past the trigger point, refresh!
        if (!state.isRefreshing && state.indicatorOffset >= refreshTrigger) {
            onRefresh()
        }

        // Reset the drag in progress state
        state.isSwipeInProgress = false

        // Don't consume any velocity, to allow the scrolling layout to fling
        return Velocity.Zero
    }
}

@Composable
fun Arrow(
    modifier: Modifier,
    color: Color = Color.Black
) {
    Canvas(modifier = modifier) {
        val path = Path()
        val width = size.width
        val height = size.height
        val lineWidth = width * 30 / 225
        val v1 = lineWidth * sin(PI / 4).toFloat()
        val v2 = lineWidth * sin(PI / 4).toFloat()

        path.moveTo(width / 2, height)
        path.lineTo(0f, height / 2)

        path.lineTo(v1, height / 2 - v1)
        path.lineTo(width / 2 - lineWidth / 2, height - v2 - lineWidth / 2)
        path.lineTo(width / 2 - lineWidth / 2, 0f)
        path.lineTo(width / 2 + lineWidth / 2, 0f)
        path.lineTo(width / 2 + lineWidth / 2, height - v2 - lineWidth / 2)
        path.lineTo(width - v1, height / 2 - v1)
        path.lineTo(width, height / 2)
        path.close()

        drawPath(
            path = path,
            color = color
        )
    }
}

@Composable
fun Loading(
    modifier: Modifier,
    color: Color = Color.Black
) {
    val showAnimator by remember { mutableStateOf(true) }
    val animatorDegrees = remember {
        Animatable(0f)
    }
    val animationSpec = infiniteRepeatable(
        animation = tween<Float>(
            durationMillis = 1000,
            easing = LinearEasing
        ),
    )

    LaunchedEffect(showAnimator) {
        animatorDegrees.animateTo(
            targetValue = 360f,
            animationSpec = animationSpec
        )
    }

    Canvas(
        modifier = modifier
    ) {
        val width = size.width
        val height = size.height

        val r = max(1f, width / 5)
        val itemCount = 12

        val path = Path()
        path.addRoundRect(
            RoundRect(
                rect = Rect(Offset(width / 2 - r / 10, 0f), Size(r / 5, r)),
                cornerRadius = CornerRadius(r / 5, r / 5)
            )
        )

        (0 until itemCount).forEach {
            val degrees = 30f * it + animatorDegrees.value
            rotate(degrees) {
                drawPath(path = path, color = color.copy(alpha = (it + 1f) / itemCount))
            }
        }
    }
}

@Composable
fun ClassicRefreshHeader(state: SwipeRefreshState) {
    val text = when (state.headerState) {
        RefreshHeaderState.PullDownToRefresh -> "下拉刷新"
        RefreshHeaderState.Refreshing -> "正在刷新..."
        else -> "释放刷新"
    }
    val angle = remember {
        Animatable(0f)
    }
    LaunchedEffect(key1 = state.headerState) {
        if (state.headerState == RefreshHeaderState.ReleaseToRefresh) {
            angle.animateTo(180f)
        } else {
            angle.animateTo(0f)
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (state.headerState == RefreshHeaderState.Refreshing) {
                Loading(
                    modifier = Modifier
                        .size(20.dp)
                        .rotate(angle.value),
                    color = MaterialTheme.colorScheme.onBackground
                )
            } else {
                Arrow(
                    modifier = Modifier
                        .size(20.dp)
                        .rotate(angle.value),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            Text(
                text = text,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .wrapContentSize()
                    .clipToBounds()
                    .padding(16.dp, 0.dp)
            )
        }
    }
}

@Composable
fun SwipeRefresh(
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    swipeStyle: SwipeRefreshStyle = SwipeRefreshStyle.Translate,
    swipeEnabled: Boolean = true,
    refreshTriggerRate: Float = 1f, //刷新生效高度与indicator高度的比例
    maxDragRate: Float = 2.5f, //最大刷新距离与indicator高度的比例
    indicator: @Composable (state: SwipeRefreshState) -> Unit = {
        ClassicRefreshHeader(it)
    },
    content: @Composable () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val updatedOnRefresh = rememberUpdatedState(onRefresh)
    var indicatorHeight by remember {
        mutableIntStateOf(1)
    }
    val refreshTrigger = indicatorHeight * refreshTriggerRate
    val maxDrag = indicatorHeight * maxDragRate
    val state = rememberSwipeRefreshState(isRefreshing, refreshTrigger, maxDrag)
    LaunchedEffect(state.isSwipeInProgress, state.isRefreshing) {
        // If there's no swipe currently in progress, animate to the correct resting position
        if (!state.isSwipeInProgress) {
            if (state.isRefreshing) {
                state.animateOffsetTo(refreshTrigger)
            } else {
                state.animateOffsetTo(0f)
            }
        }
    }

    // Our nested scroll connection, which updates our state.
    val nestedScrollConnection = remember(state, coroutineScope) {
        SwipeRefreshNestedScrollConnection(state, coroutineScope) {
            // On refresh, re-dispatch to the update onRefresh block
            updatedOnRefresh.value.invoke()
        }
    }.apply {
        this.enabled = swipeEnabled
        this.refreshTrigger = refreshTrigger
    }

    Box(
        modifier.nestedScroll(connection = nestedScrollConnection)
    ) {
        Box(modifier = Modifier
            .onGloballyPositioned {
                indicatorHeight = it.size.height
            }
            .let { if (isHeaderNeedClip(state, indicatorHeight)) it.clipToBounds() else it }
            .offset {
                getHeaderOffset(swipeStyle, state, indicatorHeight)
            }
            .zIndex(getHeaderZIndex(swipeStyle))
        ) {
            indicator(state)
        }
        Box(modifier = Modifier.offset {
            getContentOffset(swipeStyle, state)
        }) {
            content()
        }
    }
}


private fun isHeaderNeedClip(state: SwipeRefreshState, indicatorHeight: Int): Boolean {
    return state.indicatorOffset < indicatorHeight
}

private fun getHeaderZIndex(style: SwipeRefreshStyle): Float {
    return if (style == SwipeRefreshStyle.FixedFront || style == SwipeRefreshStyle.FixedContent) {
        1f
    } else {
        0f
    }
}

private fun getHeaderOffset(
    style: SwipeRefreshStyle,
    state: SwipeRefreshState,
    indicatorHeight: Int
): IntOffset {
    return when (style) {
        SwipeRefreshStyle.Translate -> {
            IntOffset(0, state.indicatorOffset.toInt() - indicatorHeight)
        }
        SwipeRefreshStyle.FixedBehind, SwipeRefreshStyle.FixedFront -> {
            IntOffset(0, 0)
        }
        else -> {
            IntOffset(0, state.indicatorOffset.toInt() - indicatorHeight)
        }
    }
}

private fun getContentOffset(
    style: SwipeRefreshStyle,
    state: SwipeRefreshState
): IntOffset {
    return when (style) {
        SwipeRefreshStyle.Translate -> {
            IntOffset(0, state.indicatorOffset.toInt())
        }
        SwipeRefreshStyle.FixedBehind -> {
            IntOffset(0, state.indicatorOffset.toInt())
        }
        else -> {
            IntOffset(0, 0)
        }
    }
}