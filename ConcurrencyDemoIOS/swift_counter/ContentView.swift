import SwiftUI

class CounterViewModel: ObservableObject {
    @Published var asyncUnsafeCounter = 0
    private var counter = 0

    func incrementAsyncUnsafeCounter() {
        asyncUnsafeCounter = 0
        counter = 0

        for _ in 0..<1000 {
            DispatchQueue.global(qos: .background).async {
                usleep(500)  // 1 millisecond delay
                self.counter += 1

                DispatchQueue.main.async {
                    self.asyncUnsafeCounter = self.counter
                }
            }
        }
    }
}

struct ContentView: View {
    @StateObject private var viewModel = CounterViewModel()

    var body: some View {
        VStack {
            Text("\(viewModel.asyncUnsafeCounter)")
                .font(.system(size: 96))
            Button(action: {
                viewModel.incrementAsyncUnsafeCounter()
            }) {
                Text("Increment!")
                    .font(.headline)
                    .foregroundColor(.white) // Text color
                    .padding() // Add padding around the text
                    .background(Color.blue) // Button background color
                    .cornerRadius(20) // Rounded corners
            }.padding()
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
