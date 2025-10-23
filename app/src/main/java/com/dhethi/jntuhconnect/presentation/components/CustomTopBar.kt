package com.dhethi.jntuhconnect.presentation.components


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.dhethi.jntuhconnect.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopBar(navController: NavController) {


    Column {
        TopAppBar(
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(MaterialTheme.shapes.medium)
                                .background(Color.Black),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_launcher),
                                contentDescription = "App icon",
                                modifier = Modifier.size(38.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))

                        Column(modifier = Modifier.height(28.dp)) {
                            Text(
                                text = stringResource(R.string.app_name).uppercase(),
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier
                                    .padding(0.dp)
                                    .height(24.dp)


                            )

                        }
                    }

                    Box(
                        contentAlignment = Alignment.TopEnd,
                        modifier = Modifier
                            .padding(end = 8.dp)

                    ) {


                    }
                }
            }
        )
        HorizontalDivider(
            thickness = 0.5.dp,
            color = Color.Gray
        )
    }
}


@Preview
@Composable
fun customTopBarPreview(){
    CustomTopBar(navController = NavController(LocalContext.current))
}
