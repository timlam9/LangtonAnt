package com.lamti.mermigkas

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.with
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.lamti.mermigkas.domain.Cell
import com.lamti.mermigkas.domain.CellColor
import com.lamti.mermigkas.domain.Grid
import com.lamti.mermigkas.ui.theme.MermigkasTheme
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

const val GRID_SIZE = 17
const val GRID_PADDING = 10
const val CELL_PADDING = 2
const val ELEVATION = 2
const val ASPECT_RATIO = 1f
const val TEXT_FONT_SIZE = 14

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val screenWidth = LocalConfiguration.current.screenWidthDp
            val boxSize by remember { mutableStateOf(screenWidth / GRID_SIZE) }

            val board: MutableState<List<Cell>> = rememberSaveable { mutableStateOf(emptyList()) }

            LaunchedEffect(Unit) {

                viewModel.board.onEach {
                    Log.d("TAGARA", "Act Grid: ${it.flatten().first { it.ant != null }}")
                    board.value = it.flatten()
                }.launchIn(lifecycleScope)
            }


            MermigkasTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    if (board.value.isNotEmpty()) {
                        MainScreen(
                            board = board.value,
                            boxSize = boxSize,
                            isLoading = false,
                            onButtonClick = { },
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MainScreen(
    board: List<Cell>,
    boxSize: Int,
    isLoading: Boolean,
    onButtonClick: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        AntGrid(
            boxes = board,
            boxSize = boxSize,
        )
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            androidx.compose.animation.AnimatedVisibility(visible = isLoading) {
                CircularProgressIndicator()
            }
            androidx.compose.animation.AnimatedVisibility(visible = !isLoading) {
                TextButton(text = "Vamos Mermigka", onClick = onButtonClick)
            }
        }
    }
}

@Composable
fun AntGrid(
    boxes: List<Cell>,
    boxSize: Int,
) {
    val gridState = rememberLazyListState()

    LazyVerticalGrid(
        cells = GridCells.Adaptive(boxSize.dp),
        contentPadding = PaddingValues(GRID_PADDING.dp),
        state = gridState
    ) {
        itemsIndexed(
            items = boxes,
        ) { index, box ->

            AntBox(box = box)
        }
    }
}

@Composable
fun AntBox(
    modifier: Modifier = Modifier,
    box: Cell,
) {
    AntBox(
        modifier = modifier,
        text = box.coordinates.toString(),
        color = if (box.ant != null) Color.Blue else {
            if (box.color == CellColor.WHITE) Color.White else Color.Black
        },
        textColor = if (box.ant != null) Color.White else Color.Blue
    )
}

@Composable
fun AntBox(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = MaterialTheme.colors.background,
    textColor: Color = MaterialTheme.colors.onBackground,
    showText: Boolean = false,
) {
    Card(
        backgroundColor = color,
        modifier = modifier
            .padding(CELL_PADDING.dp)
            .aspectRatio(ASPECT_RATIO),
        elevation = ELEVATION.dp,
    ) {
        AnimatedVisibility(visible = showText) {
            Text(
                text = text,
                fontWeight = FontWeight.Bold,
                fontSize = TEXT_FONT_SIZE.sp,
                color = textColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.wrapContentSize(Alignment.Center)
            )
        }
    }
}

@Composable
fun TextButton(modifier: Modifier = Modifier, text: String, onClick: () -> Unit) {
    Button(
        modifier = modifier,
        onClick = onClick,
        colors = ButtonDefaults.textButtonColors(backgroundColor = Color.LightGray)
    ) {
        Text(text, color = MaterialTheme.colors.onBackground)
    }
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CountDown(
    count: Int?,
    modifier: Modifier = Modifier,
) {
    val transitionDuration = 500
    val enterTransition = scaleIn(initialScale = 1.5f, animationSpec = tween(transitionDuration))
    val exitTransition = scaleOut(targetScale = 4f, animationSpec = tween(transitionDuration)) + fadeOut(
        animationSpec = tween(transitionDuration)
    )
    AnimatedVisibility(
        modifier = modifier,
        visible = count != null,
        enter = enterTransition,
        exit = exitTransition
    ) {
        val countDown = when (count) {
            0, null -> "Go"
            else -> "$count"
        }
        AnimatedContent(
            targetState = count,
            transitionSpec = { enterTransition with fadeOut(animationSpec = tween(0)) },
            contentAlignment = Alignment.Center,
        ) {
            Text(
                modifier = Modifier.padding(100.dp),
                text = countDown.uppercase(),
                color = Color.White,
                textAlign = TextAlign.Center,
            )
        }
    }
}
