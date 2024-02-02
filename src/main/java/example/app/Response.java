package example.app;

public class Response<T> {

  T result;

  public T getResult() {
    return result;
  }

  public Response<T> setResult(T result) {
    this.result = result;
    return this;
  }

  public Response(T result) {
    this.result = result;
  }
}
