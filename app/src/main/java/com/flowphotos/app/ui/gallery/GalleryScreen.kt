package com.flowphotos.app.ui.gallery

import android.Manifest
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Collections
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.flowphotos.app.data.MediaItem
import com.flowphotos.app.data.MediaRepository
import com.flowphotos.app.ui.theme.FlowBackground
import com.flowphotos.app.ui.theme.FlowCard
import com.flowphotos.app.ui.theme.FlowMuted
import com.flowphotos.app.ui.theme.FlowPrimary
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.launch

private val GlassFill = Color(0x9912121A)
private val GlassBorder = Color(0x33FFFFFF)
private val GlassHighlight = Color(0x14FFFFFF)

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun GalleryScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val repository = remember { MediaRepository(context) }

    val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        listOf(
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_VIDEO,
        )
    } else {
        listOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    }
    val permissionState = rememberMultiplePermissionsState(permissions)

    var media by remember { mutableStateOf<List<MediaItem>>(emptyList()) }
    var loading by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf(0) }

    fun load() {
        scope.launch {
            loading = true
            media = repository.loadMedia()
            loading = false
        }
    }

    LaunchedEffect(permissionState.allPermissionsGranted) {
        if (permissionState.allPermissionsGranted) {
            load()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FlowBackground),
    ) {
        // Content scrolls under glass bars
        when {
            !permissionState.allPermissionsGranted -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Allow access to your photos & videos",
                            color = FlowMuted,
                            modifier = Modifier.padding(bottom = 16.dp),
                        )
                        Button(onClick = { permissionState.launchMultiplePermissionRequest() }) {
                            Text("Grant permission")
                        }
                    }
                }
            }

            loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(color = FlowPrimary)
                }
            }

            media.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text("No photos or videos found", color = FlowMuted)
                }
            }

            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 110.dp),
                    contentPadding = PaddingValues(
                        start = 8.dp,
                        end = 8.dp,
                        top = 100.dp,
                        bottom = 110.dp,
                    ),
                    horizontalArrangement = Arrangement.spacedBy(3.dp),
                    verticalArrangement = Arrangement.spacedBy(3.dp),
                    modifier = Modifier.fillMaxSize(),
                ) {
                    items(media, key = { it.id }) { item ->
                        MediaThumb(item)
                    }
                }
            }
        }

        // Soft blur top glass
        SoftGlassTopBar(
            title = "Library",
            modifier = Modifier.align(Alignment.TopCenter),
        )

        // Soft blur bottom nav
        SoftGlassBottomBar(
            selectedIndex = selectedTab,
            onSelect = { selectedTab = it },
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }
}

@Composable
private fun SoftGlassTopBar(
    title: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        // Soft fade into content
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(28.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            FlowBackground.copy(alpha = 0.92f),
                            Color.Transparent,
                        ),
                    ),
                ),
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                GlassPill(text = "Select")
            }
        }

        // Soft bottom edge blur fade
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            FlowBackground.copy(alpha = 0.55f),
                            Color.Transparent,
                        ),
                    ),
                ),
        )
    }
}

@Composable
private fun SoftGlassBottomBar(
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Soft top edge fade
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            FlowBackground.copy(alpha = 0.45f),
                        ),
                    ),
                ),
        )

        Row(
            modifier = Modifier
                .padding(bottom = 12.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(GlassFill)
                .border(1.dp, GlassBorder, RoundedCornerShape(28.dp))
                .padding(horizontal = 10.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            GlassNavItem(
                icon = Icons.Outlined.PhotoLibrary,
                label = "Library",
                selected = selectedIndex == 0,
                onClick = { onSelect(0) },
            )
            GlassNavItem(
                icon = Icons.Outlined.Collections,
                label = "Albums",
                selected = selectedIndex == 1,
                onClick = { onSelect(1) },
            )
            GlassNavItem(
                icon = Icons.Outlined.Search,
                label = "Search",
                selected = selectedIndex == 2,
                onClick = { onSelect(2) },
            )
        }
    }
}

@Composable
private fun GlassNavItem(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val bg = if (selected) GlassHighlight else Color.Transparent
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(bg)
            .clickable(onClick = onClick)
            .padding(horizontal = 18.dp, vertical = 8.dp),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (selected) FlowPrimary else FlowMuted,
            modifier = Modifier.size(22.dp),
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = if (selected) FlowPrimary else FlowMuted,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
        )
    }
}

@Composable
private fun GlassPill(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        color = FlowPrimary,
        fontWeight = FontWeight.Medium,
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(GlassFill)
            .border(1.dp, GlassBorder, RoundedCornerShape(20.dp))
            .padding(horizontal = 14.dp, vertical = 8.dp),
    )
}

@Composable
private fun MediaThumb(item: MediaItem) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(RoundedCornerShape(6.dp))
            .background(FlowCard),
    ) {
        AsyncImage(
            model = item.uri,
            contentDescription = item.displayName,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )
        if (item.isVideo) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(6.dp)
                    .size(22.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.45f)),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "▶",
                    color = Color.White,
                    style = MaterialTheme.typography.labelSmall,
                )
            }
        }
    }
}
