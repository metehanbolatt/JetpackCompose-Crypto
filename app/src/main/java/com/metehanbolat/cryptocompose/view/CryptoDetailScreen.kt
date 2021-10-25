package com.metehanbolat.cryptocompose.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.metehanbolat.cryptocompose.model.Crypto
import com.metehanbolat.cryptocompose.util.Resource
import com.metehanbolat.cryptocompose.viewmodel.CryptoDetailViewModel
import kotlinx.coroutines.launch

@ExperimentalCoilApi
@Composable
fun CryptoDetailScreen(id: String, price: String, navController: NavController, viewModel : CryptoDetailViewModel = hiltViewModel()){

    //val cryptoItem = remember { mutableStateOf<Resource<Crypto>>(Resource.Loading()) }
    //LaunchedEffect(key1 = Unit){ cryptoItem.value = viewModel.getCrypto(id) }

    val cryptoItem = produceState<Resource<Crypto>>(initialValue = Resource.Loading()){
        value = viewModel.getCrypto(id)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.secondary),
        contentAlignment = Alignment.Center
    ){
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            when(cryptoItem.value){
                is Resource.Success -> {
                    val selectedCrypto = cryptoItem.value.data!![0]
                    Text(
                        text = selectedCrypto.name,
                        style = MaterialTheme.typography.h3,
                        modifier = Modifier.padding(2.dp),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.primary,
                        textAlign = TextAlign.Center
                    )
                    Image(
                        painter = rememberImagePainter(data = selectedCrypto.logo_url),
                        contentDescription = selectedCrypto.name,
                        modifier = Modifier
                            .padding(16.dp)
                            .size(200.dp, 200.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color.Gray, CircleShape)
                    )
                    Text(
                        text = price,
                        style = MaterialTheme.typography.h4,
                        modifier = Modifier.padding(2.dp),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.primaryVariant,
                        textAlign = TextAlign.Center
                    )
                }
                is Resource.Error -> {

                }
                is Resource.Loading -> {

                }
            }
        }
    }
}