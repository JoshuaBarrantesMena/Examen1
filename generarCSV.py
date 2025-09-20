import csv
import random

def generar_csv(nombre_archivo, filas, columnas, minimo, maximo, decimales=False):
    """
    Genera un archivo CSV con datos aleatorios.
    
    Parámetros:
    - nombre_archivo: ruta del archivo CSV de salida (string)
    - filas: número de filas (int)
    - columnas: número de columnas (int)
    - minimo: valor mínimo (int o float)
    - maximo: valor máximo (int o float)
    - decimales: si True -> números flotantes, si False -> enteros
    """
    with open(nombre_archivo, mode="w", newline="") as file:
        writer = csv.writer(file, delimiter=";")
        
        for _ in range(filas):
            fila = []
            for _ in range(columnas):
                if decimales:
                    valor = round(random.uniform(minimo, maximo), 2)  # 2 decimales
                else:
                    valor = random.randint(int(minimo), int(maximo))
                fila.append(valor)
            writer.writerow(fila)

    print(f"✅ Archivo {nombre_archivo} generado con {filas} filas y {columnas} columnas.")


if __name__ == "__main__":
    # Parámetros de prueba
    nombre = "Fortran/input.csv"
    filas = 1500
    columnas = 3
    minimo = 50
    maximo = 200
    decimales = False
    
    generar_csv(nombre, filas, columnas, minimo, maximo, decimales)