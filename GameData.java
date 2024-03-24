package ConnectFour.src;

public class GameData {
    public char[][] grid = new char[6][7];
    public boolean WHICH_CALL;

    public GameData(){
        reset();
        WHICH_CALL = true;
    }
    public char[][] getGrid(){
        return grid;
    }
    public void reset(){
        grid = new char[6][7];
        for(int i = 0; i < 6; ++i){
            for(int j = 0; j < 7; ++j){
                grid[i][j] = ' ';
            }
        }
    }
    public boolean tie(){
        if(isWinner() != ' '){
            return false;
        }
        for(int i = 0; i < 6; i++){
            for(int j = 0; j < 7; j++){
                if(grid[i][j] == ' '){
                    return false;
                }
            }
        }
        return true;
    }
    public char isWinner(){
        for(int i = 0; i < 6; i++){
            for(int j = 0; j < 4; j++){
                if(grid[i][j] != ' ' && grid[i][j] == grid[i][j+1] && grid[i][j] == grid[i][j+2] && grid[i][j] == grid[i][j+3]){
                    return grid[i][j];
                }
            }
        }
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 7; j++){
                if(grid[i][j] != ' ' && grid[i][j] == grid[i+1][j] && grid[i][j] == grid[i+2][j] && grid[i][j] == grid[i+3][j]){
                    return grid[i][j];
                }
            }
        }
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 4; j++){
                if(grid[i][j] != ' ' && grid[i][j] == grid[i+1][j+1] && grid[i][j] == grid[i+2][j+2] && grid[i][j] == grid[i+3][j+3]){
                    return grid[i][j];
                }
            }
        }
        for(int i = 0; i < 3; i++){
            for(int j = 3; j < 7; j++){
                if(grid[i][j] != ' ' && grid[i][j] == grid[i+1][j-1] && grid[i][j] == grid[i+2][j-2] && grid[i][j] == grid[i+3][j-3]){
                    return grid[i][j];
                }
            }
        }
        return ' ';
    }
}