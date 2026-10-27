package com.example.myapplication.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.layout.ContentScale
import androidx.annotation.DrawableRes

object AppColors {
    val Background = Color(0xFF1B1B1F)
    val Primary = Color(0xFF0047FF)     // títulos azuis
    val ButtonDark = Color(0xFF161B93)
    val ButtonLight = Color(0xFFE1EFFC)
    val Error = Color(0xFFC62828)
    val Stroke = Color(0xFFD9D9D9)
}

@Composable
fun AppBackground(
    scroll: Boolean = false,
    @DrawableRes fundo: Int = R.drawable.bg_shapes_completo,
    content: @Composable ColumnScope.() -> Unit
) {
    val scrollState = rememberScrollState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.Background)
    ) {
        Image(
            painter = painterResource(fundo),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .imePadding()
                .then(if (scroll) Modifier.verticalScroll(scrollState) else Modifier)
                .padding(24.dp),
            content = content
        )
    }
}
@Composable
fun PrimaryButton(text: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth().height(52.dp),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = AppColors.ButtonDark,
            contentColor = Color.White
        )
    ) { Text(text, fontWeight = FontWeight.Bold) }
}

@Composable
fun SecondaryButton(text: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth().height(52.dp),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = AppColors.ButtonLight,
            contentColor = AppColors.ButtonDark
        )
    ) { Text(text, fontWeight = FontWeight.Bold) }
}

@Composable
fun LogoHeader(titulo: String) {
    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(R.drawable.logo_unifor),
            contentDescription = "Logo Academia UNIFOR",
            modifier = Modifier.size(96.dp)
        )
        Text("Academia UNIFOR", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Spacer(Modifier.height(24.dp))
        Text(titulo, color = AppColors.Primary, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
fun AppTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    error: Boolean = false,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    var visivel by remember { mutableStateOf(false) }
    Column(modifier.fillMaxWidth()) {
        Text(label, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(placeholder, color = Color.Gray) },
            leadingIcon = { Icon(icon, contentDescription = null, tint = Color.LightGray) },
            trailingIcon = if (isPassword) {
                {
                    IconButton(onClick = { visivel = !visivel }) {
                        Icon(
                            imageVector = if (visivel) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = "Mostrar ou ocultar senha",
                            tint = Color.White
                        )
                    }
                }
            } else null,
            visualTransformation = if (isPassword && !visivel) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(
                keyboardType = if (isPassword) KeyboardType.Password else keyboardType
            ),
            singleLine = true,
            isError = error,
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                errorTextColor = Color.White,
                focusedBorderColor = Color.White,
                unfocusedBorderColor = AppColors.Stroke,
                errorBorderColor = AppColors.Error,
                cursorColor = Color.White
            )
        )
        if (error) Text("Preencha este campo.", color = AppColors.Error, fontSize = 12.sp)
    }
}
